package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.courier_location_history.routeToCourierLocation
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal


fun Route.routeToCollector() {
    val staffService = StaffService
    route("collector") {

        get("") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            println(merchantId)
            val collectors = staffService.getAllCourier(merchantId = merchantId)
            if (collectors.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(collectors)
        }

        get("/{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val collector = staffService.get(id = id, merchantId = merchantId)
            if (collector == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(collector)
        }
    }
}
