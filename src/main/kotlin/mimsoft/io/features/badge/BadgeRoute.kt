package mimsoft.io.features.badge

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToBadge() {

  val badgeService = BadgeService

  get("badges") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val badges = badgeService.getAll(merchantId = merchantId)
    call.respond(badges.ifEmpty { HttpStatusCode.NoContent })
  }

  get("badge/{id}") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val badge = badgeService.get(merchantId, id)
    call.respond(badge ?: HttpStatusCode.NoContent)
  }

  post("badge") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val badge = call.receive<BadgeDto>()
    val id = badgeService.add(badge.copy(merchantId = merchantId))
    call.respond(id)
  }

  put("badge") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val badge = call.receive<BadgeDto>()
    val updated = badgeService.update(badge.copy(merchantId = merchantId))
    call.respond(if (updated) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
  }

  delete("badge/{id}") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@delete
    }
    badgeService.delete(merchantId, id)
    call.respond(HttpStatusCode.OK)
  }
}
