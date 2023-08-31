package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.features.visit.VisitService
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToClientVisit() {

    post("visit") {
        val pr = getPrincipal()
        val userId = pr?.userId
        val visitId = call.parameters["visitId"]?.toLongOrNull()
        val merchantId = pr?.merchantId
        val visit = call.receive<VisitDto>()
        val response = VisitService.add(visit.copy(id = visitId, user = UserDto(id = userId), merchantId = merchantId))
        call.respond(response)
    }

    get("visits") {
        val pr = getPrincipal()
        val merchantId = pr?.merchantId
        val userId = pr?.userId
        val visits = VisitService.getAll(merchantId = merchantId, userId = userId)
        if (visits.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(visits)
    }
}