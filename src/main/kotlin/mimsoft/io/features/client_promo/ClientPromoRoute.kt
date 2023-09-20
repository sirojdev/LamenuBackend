package mimsoft.io.features.client_promo

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToClientPromo() {
    val service = ClientPromoService
    route("promo") {
        post("check") {
            val promoName = call.receive<PromoDto>()
            val response = service.check(promoName = promoName.name)
            if (response == null) {
                HttpStatusCode.NotAcceptable
                return@post
            }
            call.respond(HttpStatusCode.OK, response)
            return@post
        }

        post {
            val pr = call.principal<BasePrincipal>()
            val userId = pr?.userId
            val dto = call.receive<ClientPromoDto>()
            if (dto.promo?.id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val client = dto.client?.copy(id = userId)
            val response = service.add(dto = dto.copy(client = client))
            if (response == null) {
                call.respond(HttpStatusCode.NoContent)
                return@post
            }
            call.respond(response)
        }

        get("all") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val response = service.getAll(merchantId = merchantId)
            if (response.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if(id == null){
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = service.delete(id = id)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    get("promos") {
        val pr = call.principal<BasePrincipal>()
        val clientId = pr?.userId
        val response = service.getByClientId(clientId = clientId)
        if (response.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(response)
        return@get
    }
}
