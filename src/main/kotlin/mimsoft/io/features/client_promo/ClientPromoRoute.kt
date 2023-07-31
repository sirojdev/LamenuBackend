package mimsoft.io.features.client_promo

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal

fun Route.routeToClientPromo(){
    val service = ClientPromoService
    route(""){
        post {
            val dto = call.receive<ClientPromoDto>()
            if(dto.client?.id == null || dto.promo?.id == null){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val response = service.add(dto = dto)
        }

        get("promos") {
            val pr = call.principal<UserPrincipal>()
            val clientId = pr?.id
            val response = service.getByClientId(clientId = clientId)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }
    }
}
