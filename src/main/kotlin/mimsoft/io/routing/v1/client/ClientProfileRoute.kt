package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl

fun Route.routeToClientProfile() {

    val userRepository: UserRepository = UserRepositoryImpl

    authenticate("user") {
        route("profile") {

            get {
                val pr = call.principal<UserPrincipal>()
                val user = userRepository.get(id = pr?.id, merchantId = pr?.merchantId)
                call.respond(user ?: HttpStatusCode.NoContent)
            }

            put {
                val pr = call.principal<UserPrincipal>()
                val user = call.receive<UserDto>()
                userRepository.update(user.copy(id = pr?.id, merchantId = pr?.merchantId))
                call.respond(HttpStatusCode.OK)
            }

            put("firebase") {
                val pr = call.principal<UserPrincipal>()
                val device = call.receive<DeviceModel>()
                DeviceController.editFirebase(
                    sessionUUID = pr?.uuid,
                    token = device.firebaseToken
                )
                call.respond(HttpStatusCode.OK)
            }

        }



    }


}