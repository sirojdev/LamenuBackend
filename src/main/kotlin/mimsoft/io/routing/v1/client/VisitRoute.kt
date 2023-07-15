package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.features.visit.VisitService

fun Route.routeToClientVisit(){
    route("visit"){
        get{
            val pr = call.principal<UserPrincipal>()
            val userId = pr?.id
            val visit = call.receive<VisitDto>()
            val response = VisitService.add(visit.copy(user = UserDto(id = userId)))
            call.respond(HttpStatusCode.OK, VisitId(response))
            return@get
        }
    }
}
data class VisitId(
    val id: Long? = null
)