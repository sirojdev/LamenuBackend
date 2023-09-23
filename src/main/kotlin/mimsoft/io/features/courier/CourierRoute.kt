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


fun Route.routeToCourier() {
    val staffService = StaffService
    val courierService = CourierService
    route("courier") {
        routeToCourierLocation()

        get("all") {
            val principal = getPrincipal()
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val couriers = staffService.getAllCourier(merchantId = merchantId,limit,offset)
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
            val courier = courierService.get(id = id, merchantId = merchantId)
            if (courier == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(courier)
        }

        post(""){
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val dto = call.receive<CourierDto>()
            val result = courierService.add(dto.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, CourierId(result))
            return@post
        }

        put(""){
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val dto = call.receive<CourierDto>()
            val result = courierService.update(dto.copy(merchantId = merchantId))
            call.respond(result)
            return@put
        }

        delete("{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            val result = courierService.delete(id = id, merchantId = merchantId)
            call.respond(result)
            return@delete
        }
    }
}


data class CourierId(
    val id: Long? = null
)
