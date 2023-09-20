package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCollector() {
    val staffService = StaffService
    route("collector") {
        get("all") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val couriers = staffService.getAllCollector(merchantId = merchantId,limit,offset)
            if (couriers.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(couriers)
        }

        get("/{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val collector = staffService.getCollector(id = id, merchantId = merchantId)
            if (collector == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(collector)
        }
    }
}