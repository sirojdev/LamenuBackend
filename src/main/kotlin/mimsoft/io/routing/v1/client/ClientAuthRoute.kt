package mimsoft.io.routing.v1.client

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.rsa.Generator
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.rsa.Status
import mimsoft.io.services.sms.SmsSenderService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.plugins.GSON
import java.util.*

fun Route.routeToClientAuth() {

    val userRepository: UserRepository = UserRepositoryImpl
    val sessionRepository = SessionRepository
    authenticate("device") {
        post("send-sms") {
            val pr = call.principal<DevicePrincipal>()
            val merchantId = pr?.merchantId
            val deviceReceive = call.receive<DeviceModel>()
            if (deviceReceive.phone == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val phone = deviceReceive.phone
            val gn = Generator.generate(true)
            SmsSenderService.send(merchantId = pr?.merchantId, phone, "code ${gn.code}")
            DeviceController.updateCode(
                DeviceModel(
                    id = pr?.id,
                    action = "verify",
                    merchantId = merchantId,
                    code = gn.code.toString(),
                    phone = phone
                )
            )

            call.respond(
                DeviceModel(
                    id = pr?.id,
                    token = JwtConfig.generateDeviceToken(
                        merchantId = merchantId,
                        uuid = pr?.uuid,
                        hash = gn.hash,
                        phone = phone
                    )
                )
            )
        }

        post("verify") {
            val principal = call.principal<DevicePrincipal>()
            val device = call.receive<DeviceModel>()

            val checkCode = Generator.checkValidate(
                GeneratorModel(
                    hash = principal?.hash,
                    code = device.code?.toLongOrNull()
                )
            )

            if (checkCode != Status.ACCEPTED) {
                when (checkCode) {
                    Status.INVALID_CODE -> call.respond(HttpStatusCode.NotAcceptable)
                    Status.GONE_CODE -> call.respond(HttpStatusCode.Gone)
                    else -> call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                val oldCode = DeviceController.updateCode(
                    DeviceModel(
                        id = principal?.id,
                        code = null,
                        phone = null,
                        merchantId = principal?.merchantId,
                        action = "login"
                    )
                )
                if (oldCode != device.code?.toLongOrNull()) {
                    call.respond(HttpStatusCode.NotAcceptable)
                } else {
                    val user = userRepository.get(phone = principal?.phone, merchantId = principal?.merchantId)

                    if (user != null) {
                        val sessionUuid = generateSessionUUID(uuid = principal?.uuid, id = principal?.id)
                        sessionRepository.auth(
                            SessionTable(
                                uuid = sessionUuid,
                                userId = user.id,
                                deviceId = principal?.id,
                                merchantId = principal?.merchantId
                            )
                        )
                        device.token = JwtConfig.generateUserToken(
                            merchantId = principal?.merchantId,
                            uuid = sessionUuid
                        )
                        device.action = "sign-in"
                        call.respond(device)
                    } else {
                        device.token = JwtConfig.generateModifyToken(
                            sessionUuid = principal?.uuid,
                        )
                        device.action = "sign-up"
                        call.respond(device)
                    }
                }
            }
        }
    }

    authenticate("modify") {

        post("sign-up") {
            val pr = call.principal<DevicePrincipal>()
            println("modify")
            println(Gson().toJson(pr))
            val user = call.receive<UserDto>()
            val result = userRepository.add(user.copy(phone = pr?.phone, merchantId = pr?.merchantId))
            if (result.isOk()) {
                DeviceController.updateCode(
                    DeviceModel(
                        id = pr?.id,
                        merchantId = pr?.merchantId,
                        action = "",
                        code = null,
                        phone = "",
                        expAction = true
                    )
                )
                val sessionUuid = generateSessionUUID(
                    uuid = pr?.uuid,
                    id = pr?.id
                )

                SessionRepository.auth(
                    SessionTable(
                        uuid = sessionUuid,
                        merchantId = pr?.merchantId,
                        deviceId = pr?.id,
                        userId = result.body.toString().toLongOrNull()
                    )
                )

                call.respond(
                    user.copy(
                        id = result.body.toString().toLongOrNull(),
                        token = JwtConfig.generateUserToken(
                            merchantId = pr?.merchantId,
                            uuid = sessionUuid
                        )
                    )
                )
            } else {
                call.respond(result)
            }
        }
    }


}

fun generateSessionUUID(uuid: String?, id: Long?): String {
    return uuid + UUID.randomUUID() + "-" + id
}