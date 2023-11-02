package mimsoft.io.features.area

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToArea() {
  val service = AreaService
  route("area") {
    post {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val dto = call.receive<AreaDto>()
      val response = service.add(dto = dto.copy(merchantId = merchantId, branchId = branchId))
      if (!response) {
        call.respond(HttpStatusCode.NoContent)
        return@post
      }
      call.respond(HttpStatusCode.OK)
    }
    put {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val dto = call.receive<AreaDto>()
      val response = service.update(dto = dto.copy(merchantId = merchantId, branchId = branchId))
      if (!response) {
        call.respond(HttpStatusCode.NoContent)
        return@put
      }
      call.respond(HttpStatusCode.OK)
    }

    get("{id}") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      val response = service.get(id = id, merchantId = merchantId, branchId = branchId)
      if (response == null) {
        call.respond(HttpStatusCode.NotFound)
        return@get
      }
      call.respond(HttpStatusCode.OK, response)
    }

    get {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val response = service.getAll(merchantId = merchantId, branchId = branchId)
      if (response.isEmpty()) {
        call.respond(HttpStatusCode.NotFound)
        return@get
      }
      call.respond(HttpStatusCode.OK, response)
    }

    delete("{id}") {
      val pr = getPrincipal()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      val response = service.delete(id = id, merchantId = merchantId, branchId = branchId)
      if (!response) {
        call.respond(HttpStatusCode.NoContent)
        return@delete
      }
      call.respond(HttpStatusCode.OK)
    }
  }
}
