package mimsoft.io.client.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl

fun Route.routeToUser() {

  val userRepository: UserRepository = UserRepositoryImpl

  get("users") {
    val users = userRepository.getAll()
    call.respond(users)
    return@get
  }

  get("user/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val user = userRepository.get(id)
    if (user == null) {
      call.respond(HttpStatusCode.NotFound)
      return@get
    }
    call.respond(user)
  }

  post("user") {
    println("\nuser post")
    try {
      val user = call.receive<UserDto>()

      val status = userRepository.add(user)
      println("status: $status")

      call.respond(status.httpStatus, status.body ?: status.httpStatus.description)
    } catch (e: Exception) {
      e.printStackTrace()
      call.respond(HttpStatusCode.BadRequest)
    }
  }

  put("user") {
    val user = call.receive<UserDto>()
    call.respond(userRepository.update(user))
  }

  delete("user/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@delete
    }
    userRepository.delete(id)
    call.respond(HttpStatusCode.OK)
  }
}