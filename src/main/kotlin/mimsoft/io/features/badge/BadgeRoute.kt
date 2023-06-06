package mimsoft.io.features.badge

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToBadge() {

    val badgeService = BadgeService

    get("badges") {
        val merchantId = 1L
        val badges = badgeService.getAll(merchantId = merchantId)
        call.respond(badges.ifEmpty { HttpStatusCode.NoContent })
    }

    get("badge/{id}") {
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val badge = badgeService.get(merchantId, id)
        call.respond(badge?: HttpStatusCode.NoContent)
    }

    post("badge") {
        val merchantId = 1L
        val badge = call.receive<BadgeDto>()
        val id = badgeService.add(badge.copy(merchantId = merchantId))
        call.respond(id?: HttpStatusCode.InternalServerError)
    }

    put("badge") {
        val merchantId = 1L
        val badge = call.receive<BadgeDto>()
        val updated = badgeService.update(badge.copy(merchantId = merchantId))
        call.respond(if (updated) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
    }

    delete("badge/{id}") {
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        badgeService.delete(id, merchantId)
        call.respond(HttpStatusCode.OK)
    }
}