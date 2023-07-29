package mimsoft.io.routing.v1.stuff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.rsa.Generator
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.rsa.Status
import mimsoft.io.services.sms.SmsSenderService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import java.util.*

fun Route.routeToStaffAuth() {
    val staffRepository = StaffService
    val sessionRepository = SessionRepository
    authenticate("device") {
        route("auth") {
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
                        val stuff =
                            staffRepository.getByPhone(phone = principal?.phone, merchantId = principal?.merchantId)

                        if (stuff != null) {
                            val sessionUuid = generateSessionUUID(uuid = principal?.uuid, id = principal?.id)
                            sessionRepository.auth(
                                SessionTable(
                                    uuid = sessionUuid,
                                    stuffId = stuff.id,
                                    deviceId = principal?.id,
                                    merchantId = principal?.merchantId
                                )
                            )
                            device.token = JwtConfig.generateStaffToken(
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

                val staff = call.receive<StaffDto>()
                val result = staffRepository.add(staff.copy(phone = pr?.phone, merchantId = pr?.merchantId))
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
                            stuffId = result.body.toString().toLongOrNull()
                        )
                    )



                    call.respond(
                        staff.copy(
                            id = result.body.toString().toLongOrNull(),
                            token = JwtConfig.generateStaffToken(
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
}

fun generateSessionUUID(uuid: String?, id: Long?): String {
    return uuid + UUID.randomUUID() + "-" + id
}