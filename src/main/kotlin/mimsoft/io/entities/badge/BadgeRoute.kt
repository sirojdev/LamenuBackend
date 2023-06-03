package mimsoft.io.entities.badge

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToBadge() {

    val badgeService = BadgeService

    get("badges") {
        val badges = badgeService.getAll()
        call.respond(badges.ifEmpty { HttpStatusCode.NoContent })
    }

    get("badge/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val badge = badgeService.get(id)
        call.respond(badge?: HttpStatusCode.NoContent)
    }

    post("badge") {
        val badge = call.receive<BadgeDto>()
        val id = badgeService.add(badge)
        call.respond(id?: HttpStatusCode.InternalServerError)
    }

    put("badge") {
        val badge = call.receive<BadgeDto>()
        val updated = badgeService.update(badge)
        call.respond(if (updated) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
    }

    delete("badge/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        badgeService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}