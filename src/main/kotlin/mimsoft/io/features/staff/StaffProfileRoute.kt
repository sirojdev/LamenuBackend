package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToStaffProfile() {
  val staffService = StaffService
  route("profile") {
    get {
      val pr = call.principal<StaffPrincipal>()
      val staffId = pr?.staffId
      //            val staffInf = call.receive<StaffDto>()
      //            if(staffInf.phone == null || staffInf.password == null){
      //                call.respond(HttpStatusCode.BadRequest)
      //            }
      val response = staffService.getById(id = staffId)
      if (response == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(response)
      return@get
    }
  }
}
