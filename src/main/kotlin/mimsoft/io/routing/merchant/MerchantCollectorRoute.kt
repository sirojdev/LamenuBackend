package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCollector() {
  val staffService = StaffService
  route("collector") {
    get("") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val collectors =
        staffService.getAllCourier(
          merchantId = merchantId,
          limit = limit,
          offset = offset,
          branchId = branchId
        )
      if (collectors.data?.isEmpty() == true) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      } else call.respond(collectors)
    }

    get("/{id}") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val collector = staffService.get(id = id, merchantId = merchantId)
      if (collector == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(collector)
    }
  }
}
