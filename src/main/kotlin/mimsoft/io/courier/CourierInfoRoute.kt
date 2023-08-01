package mimsoft.io.courier

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.StaffPrincipal

fun Route.routeToCouriersInfo() {
    val courierService = CourierService
    get() {
        val principal = call.principal<StaffPrincipal>()
        val courierId = principal?.staffId
        val dto = courierService.getById(courierId)
        if (dto == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(dto)
        }
    }
    route("orders") {
        get("open") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId

            val orderList = CourierInfoService.getOrdersBySomething(1, "OPEN", courierId)
            if(orderList.isNullOrEmpty()){
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("active") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val orderList = CourierInfoService.getOrdersBySomething(1, "IN_PROGRESS", courierId)
            if(orderList.isNullOrEmpty()){
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("archive") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val orderList = CourierInfoService.getOrdersBySomething(1, "DELIVERY", courierId)
            if(orderList.isNullOrEmpty()){
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
    }
}