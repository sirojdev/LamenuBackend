package mimsoft.io.features.extra

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.extra.ropository.ExtraRepository
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToExtra() {

  val extraRepository: ExtraRepository = ExtraRepositoryImpl
  val mapper = ExtraMapper
  get("extras") {
    val pr = getPrincipal()
    val merchantId = pr?.merchantId
    val branchId = pr?.branchId
    val extras =
      extraRepository.getAll(merchantId = merchantId, branchId = branchId).map {
        mapper.toExtraDto(it)
      }
    if (extras.isEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    }
    call.respond(HttpStatusCode.OK, extras)
  }

  get("extra/{id}") {
    val pr = getPrincipal()
    val merchantId = pr?.merchantId
    val branchId = pr?.branchId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val extra =
      mapper.toExtraDto(extraRepository.get(id = id, merchantId = merchantId, branchId = branchId))
    if (extra != null) {
      call.respond(HttpStatusCode.OK, extra)
    } else {
      call.respond(HttpStatusCode.NotFound)
    }
  }

  post("extra") {
    val pr = getPrincipal()
    val merchantId = pr?.merchantId
    val branchId = pr?.branchId
    val extra = call.receive<ExtraDto>()
    println(extra)
    val id =
      extraRepository.add(
        mapper.toExtraTable(extra.copy(merchantId = merchantId, branchId = branchId))
      )
    call.respond(HttpStatusCode.OK, ExtraId(id))
  }

  put("extra") {
    val pr = getPrincipal()
    val extra = call.receive<ExtraDto>()
    val merchantId = pr?.merchantId
    val branchId = pr?.branchId
    extraRepository.update(extra.copy(merchantId = merchantId, branchId = branchId))
    call.respond(HttpStatusCode.OK)
  }

  delete("extra/{id}") {
    val pr = getPrincipal()
    val merchantId = pr?.merchantId
    val branchId = pr?.branchId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id != null) {
      val deleted = extraRepository.delete(id = id, merchantId = merchantId, branchId = branchId)
      if (deleted) {
        call.respond(HttpStatusCode.OK)
      } else {
        call.respond(HttpStatusCode.InternalServerError)
      }
    } else {
      call.respond(HttpStatusCode.BadRequest)
    }
  }
}

data class ExtraId(val id: Long? = null)
