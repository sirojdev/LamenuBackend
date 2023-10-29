package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.min
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToUserUser() {

  val userRepository: UserRepository = UserRepositoryImpl

  get("users") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    val search = call.parameters["search"]
    val filters = call.parameters["filters"]
    val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
    val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
    val users =
      userRepository.getAll(
        merchantId = merchantId,
        search = search,
        filters = filters,
        limit = limit,
        offset = offset,
      )
    if (users.data?.isNotEmpty() == true) {
      call.respond(users)
      return@get
    }
    call.respond(HttpStatusCode.NotFound)
  }

  post("user") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    try {
      val user = call.receive<UserDto>()

      userRepository.add(user.copy(merchantId = merchantId)).let {
        if (it.body == null) call.respond(it.httpStatus) else call.respond(it.httpStatus, it.body)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      call.respond(HttpStatusCode.BadRequest)
    }
  }

  put("user") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    val user = call.receive<UserDto>()
    val status = userRepository.update(user.copy(merchantId = merchantId))
    if (status.body as Boolean) call.respond(status)
    call.respond(HttpStatusCode.NoContent)
  }

  delete("user/{id}") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@delete
    }
    val response = userRepository.delete(id = id, merchantId = merchantId)
    if (response) call.respond(response)
    call.respond(HttpStatusCode.NoContent)
  }

  get("user/{id}") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val user = userRepository.get(id = id, merchantId = merchantId)
    if (user == null) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    }
    call.respond(user)
  }
}
