package mimsoft.io.features.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.courier_location_history.routeToCourierLocation
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import kotlin.math.min


fun Route.routeToCourier() {
    val staffService = StaffService
    val courierService = CourierService
    route("courier") {
        routeToCourierLocation()

        get("all") {
            val principal = getPrincipal()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val search = call.parameters["search"]
            val filters = call.parameters["filters"]
            val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val couriers = staffService.getAllCourier(
                merchantId = merchantId,
                branchId = branchId,
                search = search,
                filters = filters,
                limit = limit,
                offset = offset
            )
            if (couriers.data?.isNotEmpty() == true) {
                call.respond(couriers)
                return@get
            }
            call.respond(HttpStatusCode.NoContent)
        }

        get("/{id}") {
            val principal = call.principal<BasePrincipal>()
            val id = call.parameters["id"]?.toLongOrNull()
            val merchantId = principal?.merchantId
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courier = courierService.get(
                id = id,
                merchantId = merchantId,
            )
            if (courier.isNotEmpty()) {
                call.respond(courier)
                return@get
            }
            call.respond(HttpStatusCode.NoContent)
        }

        get("orders/{courierId}") {
            val principal = call.principal<BasePrincipal>()
            val courierId = call.parameters["courierId"]?.toLongOrNull()
            val merchantId = principal?.merchantId
            val search = call.parameters["search"]
            val filters = call.parameters["filters"]
            val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            if (courierId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val courier = courierService.getCourierAllOrders(
                courierId = courierId,
                merchantId = merchantId,
                filters = filters,
                limit = limit,
                offset = offset,
            )
            if (courier.isNotEmpty()) {
                call.respond(courier)
                return@get
            }
            call.respond(HttpStatusCode.NoContent)
        }

        post("") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val dto = call.receive<CourierDto>()
            val result = courierService.add(dto.copy(merchantId = merchantId, branchId = branchId))
            call.respond(HttpStatusCode.OK, CourierId(result))
            return@post
        }

        put("") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val dto = call.receive<CourierDto>()
            val result = courierService.update(dto.copy(merchantId = merchantId, branchId = branchId))
            call.respond(result)
            return@put
        }

        delete("{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val branchId = principal?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            val result = courierService.delete(id = id, merchantId = merchantId, branchId = branchId)
            call.respond(result)
            return@delete
        }
    }
}


data class CourierId(
    val id: Long? = null
)
