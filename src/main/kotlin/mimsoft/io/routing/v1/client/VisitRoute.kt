package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.features.visit.VisitService
import mimsoft.io.utils.principal.BasePrincipal
import okhttp3.internal.userAgent

fun Route.routeToClientVisit() {
    route("visit") {
        post {
            val pr = call.principal<BasePrincipal>()
            val userId = pr?.userId
            val merchantId = pr?.merchantId
            val visit = call.receive<VisitDto>()
            val response = VisitService.add(visit.copy(user = UserDto(id = userId), merchantId = merchantId))
            if (response == null) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            } else
                call.respond(HttpStatusCode.OK, VisitId(response))
        }

        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val userId = pr?.userId
            val visits = VisitService.getAll(merchantId = merchantId, userId = userId)
            if (visits.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(visits)
        }

    }
}

data class VisitId(
    val id: Long? = null
)