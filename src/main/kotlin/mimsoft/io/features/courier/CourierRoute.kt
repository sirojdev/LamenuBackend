package mimsoft.io.features.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.repository.BranchService
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.courier.courier_location_history.routeToCourierLocation
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal


fun Route.routeToCourier() {
    val staffService = StaffService
    route("courier") {
        routeToCourierLocation()

        get("") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val couriers = staffService.getAllCourier(merchantId = merchantId)
            if (couriers.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(couriers)
        }

        get("/{id}") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courier = staffService.getCourier(id = id, merchantId = merchantId)
            if (courier == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(courier)
        }
        }

}
