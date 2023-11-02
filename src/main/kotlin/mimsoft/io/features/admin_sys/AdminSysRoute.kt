package mimsoft.io.features.admin_sys

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.adminSysRoute() {
  post("auth") {
    val dto = call.receive<AdminSys>()
    val response = AdminSysService.auth(dto = dto)
    if (response.httpStatus != HttpStatusCode.OK) {
      call.respond(response.httpStatus)
      return@post
    }
    call.respond(response)
  }

  authenticate("admin") {
    get {
      val pr = getPrincipal()
      val phone = pr?.phone
      val response = AdminSysService.getAdminProfile(phone = phone)
      call.respond(response)
    }

    put {
      val pr = getPrincipal()
      val phone = pr?.phone
      val model = call.receive<AdminSys>()
      val response = AdminSysService.updateAdmin(adminModel = model.copy(phone = phone))
      call.respond(response)
    }
  }
}
