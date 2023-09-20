package mimsoft.io.entities.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.client.repository.UserRepository
import mimsoft.io.entities.client.repository.UserRepositoryImpl

fun Route.routeToUser() {

    val userRepository: UserRepository = UserRepositoryImpl

    get("users") {
        val users = userRepository.getAll().map { UserMapper.toUserDto(it) }
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
        val user = UserMapper.toUserDto(userRepository.get(id))
        if (user==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(user)
    }

    post("user") {
        try {
            val user = call.receive<UserDto>()
            val id = userRepository.add(UserMapper.toUserTable(user))
            call.respond(HttpStatusCode.OK, UserId(id))
        }catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    put("user") {
        val user = call.receive<UserDto>()
        userRepository.update(UserMapper.toUserTable(user))
        call.respond(HttpStatusCode.OK)
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

data class UserId(
    val id: Long? = null
)