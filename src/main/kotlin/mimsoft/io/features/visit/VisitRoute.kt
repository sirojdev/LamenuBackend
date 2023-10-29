package mimsoft.io.features.visit

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToVisits() {
  val visitService = VisitService
  route("visit") {
    get {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val visits = visitService.getAll(merchantId = merchantId, branchId = branchId)
      if (visits.isEmpty()) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      } else call.respond(visits)
    }

    post {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val visit = call.receive<VisitDto>()
      val id = visitService.add(visit.copy(merchantId = merchantId, branchId = branchId))
      if (id == null) {
        call.respond(HttpStatusCode.Conflict)
        return@post
      } else call.respond(id)
    }

    get("/{id}") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val visit = visitService.get(id = id, merchantId = merchantId)
      if (visit == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(visit)
    }

    put {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val visit = call.receive<VisitDto>()
      val response = visitService.update(visit.copy(merchantId = merchantId, branchId = branchId))
      call.respond(response)
    }

    delete("{id}") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val response = visitService.delete(id = id, merchantId = merchantId, branchId = branchId)
      call.respond(response)
    }
  }
}
