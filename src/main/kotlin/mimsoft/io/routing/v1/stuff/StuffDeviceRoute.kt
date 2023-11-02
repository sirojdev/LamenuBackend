package mimsoft.io.routing.v1.stuff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.session.SessionRepository

fun Route.routeToStaffDevice() {

  route("device") {
    get {
      val pr = call.principal<StaffPrincipal>()
      val devices =
        SessionRepository.getStuffDevices(
          merchantId = pr?.merchantId,
          uuid = pr?.uuid,
          stuffId = pr?.staffId
        )
      call.respond(devices)
    }
    /** terminate single device */
    post("terminate") {
      val id = call.parameters["id"]?.toLongOrNull()
      val pr = call.principal<StaffPrincipal>()
      SessionRepository.expireOtherStuff(
        uuid = pr?.uuid,
        merchantId = pr?.merchantId,
        stuffId = pr?.staffId,
        deviceId = id
      )
      call.respond(HttpStatusCode.OK)
    }
    /** terminate other devices */
    post("terminate/others") {
      val id = call.parameters["id"]?.toLongOrNull()
      val pr = call.principal<StaffPrincipal>()
      SessionRepository.expireOtherStaffs(
        uuid = pr?.uuid,
        merchantId = pr?.merchantId,
        stuffId = pr?.staffId,
      )
      call.respond(HttpStatusCode.OK)
    }
  }
}
