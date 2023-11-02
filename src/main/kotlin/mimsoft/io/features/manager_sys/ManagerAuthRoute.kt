package mimsoft.io.features.manager_sys

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToManagerAuth() {
  post("auth") {
    val dto = call.receive<ManagerSysModel>()
    val response = ManagerSysService.auth(dto = dto)
    if (response.httpStatus != HttpStatusCode.OK) {
      call.respond(response.httpStatus)
      return@post
    }
    call.respond(response)
  }
}
