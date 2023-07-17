package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.favourite.merchant
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.features.visit.VisitService
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToClientVisit(){
    route("visit"){
        post{
            val pr = call.principal<UserPrincipal>()
            val userId = pr?.id
            val merchantId = pr?.merchantId
            val visit = call.receive<VisitDto>()
            val response = VisitService.add(visit.copy(user = UserDto(id = userId, merchantId = merchantId)))
            call.respond(HttpStatusCode.OK, VisitId(response))
            return@post
        }
        get {
            val pr = call.principal<UserPrincipal>()
            val merchantId = pr?.merchantId
            val visits = VisitService.getAll(merchantId = merchantId)
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