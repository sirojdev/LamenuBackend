package mimsoft.io.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.device.DeviceController
import mimsoft.io.device.DeviceTable
import mimsoft.io.entities.client.UserDto
import mimsoft.io.entities.client.UserTable
import mimsoft.io.entities.client.repository.UserRepository
import mimsoft.io.entities.client.repository.UserRepositoryImpl
import mimsoft.io.rsa.Generator
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.rsa.Status
import mimsoft.io.services.SmsService
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.repository.Mapper
import mimsoft.io.utils.Role
import mimsoft.io.utils.plugins.GSON

fun Route.routeToLogin() {


    post("login") {
        val loginRequest = call.receive<LoginRequest>()
        println("\nloginRequest-->${GSON.toJson(loginRequest)}")

        if (loginRequest.phone == null) {
            call.respond(HttpStatusCode.BadRequest, "Phone must not be null")
            return@post
        }

        if (loginRequest.deviceUuid == null) {
            call.respond(HttpStatusCode.BadRequest, "Device uuid must not be null")
            return@post
        }

        val smsService: SmsService = SmsService
        val generator = Generator

        val generated = generator.generate()

        smsService.send(loginRequest.phone, code = generated.code)

        val deviceId = DeviceController.add(
            DeviceTable(
                uuid = loginRequest.deviceUuid
            )
        )
        val response = LoginResponse(
            loginToken = JwtConfig.generateLoginToken(
                deviceId = deviceId,
                phone = loginRequest.phone,
                hash = generated
            )
        )

        call.respond(response)
    }

    authenticate("auth") {

        post("verify") {
            val principal = call.principal<LoginPrincipal>()
            val loginRequest = call.receive<LoginRequest>()

            val generator = Generator
            val userRepository: UserRepository = UserRepositoryImpl
            val sessionRepository = SessionRepository

            val status = generator.validate(
                GeneratorModel(code = loginRequest.code, hash = principal?.hash))

            when(status){

                Status.INVALID_CODE -> {
                    call.respond(HttpStatusCode(418, "Invalid code"))
                    return@post
                }

                Status.GONE_CODE -> {
                    call.respond(HttpStatusCode(419, "Gone code"))
                    return@post
                }
                Status.ACCEPTED -> {

                    val user = userRepository.get(principal?.phone)

                    if (user != null) {

                        val session = sessionRepository.get(
                            phone = principal?.phone, deviceId = principal?.deviceId)

                        if (session==null) {

                            val sessionUuid = sessionRepository.generateUuid()

                            val newSession = SessionTable(
                                uuid = sessionUuid,
                                phone = principal?.phone,
                                deviceId = principal?.deviceId,
                                userId = user.id,
                                role = Role.USER.name,
                                isExpired = false
                            )
                            sessionRepository.add(newSession)
                        }

                        sessionRepository.refresh(session?.uuid)

                        val loginResponse = LoginResponse(
                            accessToken = JwtConfig.generateAccessToken(
                                entityId = user.id,
                                roles = arrayListOf(Role.USER)
                            ),
                            refreshToken = JwtConfig.generateRefreshToken(
                                entityId = user.id,
                                roles = arrayListOf(Role.USER)
                            )
                        )

                        call.respond(HttpStatusCode.OK, loginResponse)
                        return@post
                    }

                    val loginResponse = LoginResponse(
                        regToken = JwtConfig.generateRegToken(
                            deviceId = principal?.deviceId,
                            phone = principal?.phone
                        )
                    )

                    call.respond(HttpStatusCode(208, "User not found"), loginResponse)
                }
            }

        }

    }

    authenticate("reg"){

        post("reg") {
            val principal = call.principal<LoginPrincipal>()
            val user = call.receive<UserDto>()

            val sessionRepository = SessionRepository

            if (user.firstName == null) {
                call.respond(HttpStatusCode.BadRequest, "firstName must not be null")
            }

            val userRepository: UserRepository = UserRepositoryImpl
            val mapper = Mapper

            val status = userRepository.add(user.copy(phone = principal?.phone))

            val userBody = status.body as UserTable

            val sessionUuid = sessionRepository.generateUuid()

            val newSession = SessionTable(
                uuid = sessionUuid,
                phone = principal?.phone,
                deviceId = principal?.deviceId,
                userId = userBody.id,
                role = Role.USER.name,
                isExpired = false
            )
            sessionRepository.add(newSession)

            val loginResponse = LoginResponse(
                accessToken = JwtConfig.generateAccessToken(
                    entityId = userBody.id,
                    roles = arrayListOf(Role.USER)
                ),
                refreshToken = JwtConfig.generateRefreshToken(
                    entityId = userBody.id,
                    roles = arrayListOf(Role.USER)
                )
            )

            call.respond(HttpStatusCode.OK, loginResponse)
        }
    }

}

data class LoginResponse(
    val loginToken: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val regToken: String? = null
)

data class LoginRequest(
    val deviceUuid: String? = null,
    val phone: String? = null,
    val code: Long? = null
)