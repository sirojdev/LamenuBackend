package mimsoft.io.courier.info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCouriersInfo() {
    val courierService = CourierService
    get("profile") {
        val principal = call.principal<BasePrincipal>()
        val courierId = principal?.staffId
        val dto = courierService.getById(courierId)
        if (dto == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(dto)
        }
    }
    get("firebase") {
        val principal = call.principal<BasePrincipal>()
        val uuid = principal?.uuid
        val dto = DeviceController.getWithUUid(uuid = uuid)
        if (dto == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(dto)
        }
    }
}