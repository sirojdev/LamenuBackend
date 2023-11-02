package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.rsa.Generator
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.rsa.Status
import mimsoft.io.utils.principal.BasePrincipal

fun Route.updatePhoneRoute() {
  val userRepository: UserRepository = UserRepositoryImpl

  post("profile/verify") {
    val principal = call.principal<BasePrincipal>()
    val device = call.receive<DeviceModel>()
    val checkCode =
      Generator.checkValidate(
        GeneratorModel(hash = principal?.hash, code = device.code?.toLongOrNull())
      )

    if (checkCode != Status.ACCEPTED) {
      when (checkCode) {
        Status.INVALID_CODE -> call.respond(HttpStatusCode.NotAcceptable)
        Status.GONE_CODE -> call.respond(HttpStatusCode.Gone)
        else -> call.respond(HttpStatusCode.BadRequest)
      }
    } else {
      val rs = userRepository.updatePhone(principal?.userId, principal?.phone)
      if (rs) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
    }
  }
}
