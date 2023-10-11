package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal
import kotlin.math.min

fun Route.routeToCollector() {
    val staffService = StaffService
    route("collector") {
        get("all") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val couriers = staffService.getAllCollector(merchantId = merchantId, branchId = branchId, limit, offset)
            if (couriers.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(couriers)
        }

        get("/{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val collector = staffService.getCollector(id = id, merchantId = merchantId, branchId = branchId)
            if (collector == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(collector)
        }
    }
}