package mimsoft.io.device

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.rsa.Generator
import mimsoft.io.services.SmsService
import mimsoft.io.services.SmsServiceIml
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.Mapper
import mimsoft.io.utils.Role
import mimsoft.io.utils.authorize

fun Route.routeToLogin() {


        post("login") {
            val device = call.receive<DeviceDto>()

//        if (device.phone == null) {
//            call.respond(HttpStatusCode.BadRequest, "Phone must not be null")
//            return@post
//        }
//
//        if (device.uuid == null) {
//            call.respond(HttpStatusCode.BadRequest, "Device uuid must not be null")
//            return@post
//        }

            val smsService: SmsService = SmsServiceIml
            val mapper = Mapper
            val generator = Generator

            smsService.send(device.phone)

            val deviceId = DeviceController.add(mapper.toTable<DeviceDto, DeviceTable>(device))
            val response = LoginResponse(
                deviseToken = JwtConfig.generateDeviceToken(
                    deviceId = deviceId ?: 0,
                    uuid = device.uuid,
                    code = generator.generate()
                )
            )

            call.respond(response)
        }

}

data class LoginResponse(
    val deviseToken: String
)

data class A(
    val b: String? = null,
    val c: String? = null
)

fun main() {

    val a = A()

    when {
        a.b == null -> {
            println("b==null")
        }

        a.c == null -> {
            println("c==null")
        }
    }
}