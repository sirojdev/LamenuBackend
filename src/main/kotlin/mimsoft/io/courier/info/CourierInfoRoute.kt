package mimsoft.io.courier.info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.StaffPrincipal

fun Route.routeToCouriersInfo() {
    val courierService = CourierService
    get("profile") {
        val principal = call.principal<StaffPrincipal>()
        val courierId = principal?.staffId
        val dto = courierService.getById(courierId)
        if (dto == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(dto)
        }
    }
}