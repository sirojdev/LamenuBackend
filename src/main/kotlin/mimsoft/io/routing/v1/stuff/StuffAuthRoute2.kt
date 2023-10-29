package mimsoft.io.routing.v1.stuff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffPrincipal

fun Route.staffAuthRoute2() {
  val authService: StaffAuthService = StaffAuthImp

  post("auth") {
    val staff = call.receive<StaffDto>()
    val staffAuth = authService.auth(staff)
    if (staffAuth != null) {
      call.respond(staffAuth)
    } else call.respond(HttpStatusCode.BadRequest)
  }

  authenticate("staff") {
    post("logout") {
      val staff = call.principal<StaffPrincipal>()
      authService.logout(staff?.uuid)
      call.respond(HttpStatusCode.OK)
    }
  }
}
