package mimsoft.io.client.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.config.timestampValidator
import mimsoft.io.utils.OK

fun Route.routeToUser() {

    val userRepository: UserRepository = UserRepositoryImpl

    get("users") {
        val users = userRepository.getAll()
        if (users.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        else call.respond(users)
    }

    get("user/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val user = userRepository.get(id)
        if (user==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(user)
    }

    post("user") {
        println("\nuser post")
        try {
            val user = call.receive<UserDto>()

            val statusTimestamp = timestampValidator(user.birthDay)

            if (statusTimestamp.httpStatus != OK){
                call.respond(statusTimestamp)
                return@post
            }

            val status = userRepository.add(user)
            println("status: $status")

            call.respond(
                status.httpStatus,
                status.body?: status.httpStatus.description)
        }catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    put("user") {
        val user = call.receive<UserDto>()

        val statusTimestamp = timestampValidator(user.birthDay)

        if (statusTimestamp.httpStatus != OK){
            call.respond(statusTimestamp)
            return@put
        }

        val status = userRepository.update(user)

        call.respond(status.httpStatus, status)
    }

    delete("user/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        userRepository.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}
