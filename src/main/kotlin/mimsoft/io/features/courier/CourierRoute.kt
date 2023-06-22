package mimsoft.io.features.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.courier_location_history.routeToCourierLocation
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal


fun Route.routeToCourier() {
    val staffService = StaffService
    route("courier") {
        routeToCourierLocation()

        get("all") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            println(merchantId)
            val couriers = staffService.getAll(merchantId = merchantId)
            if (couriers.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(couriers)
        }
//OrderRepositoryImpl.getAll(merchantId = merchantId, courierId = id)
        get("/{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courier = staffService.get(id = id, merchantId = merchantId)
            if (courier == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(courier)
        }
    }
}
