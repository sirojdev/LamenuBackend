package mimsoft.io.features.visit

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.SOME_THING_WRONG
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToVisits() {
    val visitService = VisitService
    route("visit") {
        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val visits = visitService.getAll(merchantId = merchantId)
            if (visits.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(visits)
        }

        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val visit = call.receive<VisitDto>()
            val id = visitService.add(visit.copy(merchantId = merchantId))
            if(id == null){
                call.respond(HttpStatusCode.Conflict)
                return@post
            }
            else call.respond(id)
        }

        get("/{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val visit = visitService.get(id = id, merchantId = merchantId)
            if(visit==null){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(visit)
        }

        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val visit = call.receive<VisitDto>()
            val response = visitService.update(visit.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, response)
        }

        delete("{id}"){
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if(id==null){
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = visitService.delete(id = id, merchantId = merchantId)
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

data class VisitId(
    val id: Long? = null
)