package mimsoft.io.waiter.info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.waiter.WaiterService

fun Route.routeToWaitersInfo() {
    val waiterService = WaiterService
    get("profile") {
        val principal = call.principal<BasePrincipal>()
        val courierId = principal?.staffId
        val dto = waiterService.getById(courierId)
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

    patch("update") {
        val principal = call.principal<BasePrincipal>()
        val dto = call.receive<StaffDto>()
        call.respond(waiterService.updateWaiterInfo(dto.copy(id = principal?.staffId)))
    }
}