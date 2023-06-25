package mimsoft.io.features.pantry

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToPantry() {
    val pantryService = PantryService
    route("pantry") {
        post {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val pantryDto = call.receive<PantryDto>()
            val result = pantryService.check(pantryDto.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, result)
        }


        get("{id}"){
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            val response = pantryService.get(id = id, merchantId = merchantId)
            val dto = PantryMapper.toDto(response)
            call.respond(dto)
            return@get
        }

        get(""){
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val response = pantryService.getAll(merchantId = merchantId)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }

        delete("{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            val response = pantryService.delete(id = id, merchantId = merchantId)
            call.respond(response)
            return@delete

        }
    }
}

data class PantryId(
    val id: Long? = null
)