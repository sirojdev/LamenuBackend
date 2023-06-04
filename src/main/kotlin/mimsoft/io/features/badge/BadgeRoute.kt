package mimsoft.io.features.badge

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToBadge() {

    val badgeService = BadgeService

    get("badges") {
        val badges = BadgeService.getAll()
        call.respond(badges.ifEmpty { HttpStatusCode.NoContent })
    }

    get("badge/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val badge = BadgeService.get(id)
        call.respond(badge?: HttpStatusCode.NoContent)
    }

    post("badge") {
        val badge = call.receive<BadgeDto>()
        val id = BadgeService.add(badge)
        call.respond(id?: HttpStatusCode.InternalServerError)
    }

    put("badge") {
        val badge = call.receive<BadgeDto>()
        val updated = BadgeService.update(badge)
        call.respond(if (updated) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
    }

    delete("badge/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        BadgeService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}