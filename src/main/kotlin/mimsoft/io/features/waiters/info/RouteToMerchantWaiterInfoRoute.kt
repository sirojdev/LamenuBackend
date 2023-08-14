package mimsoft.io.features.waiters.info

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToMerchantWaiterInfoRoute() {
    route("info") {
        get("") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val staff = StaffService.get(id = id, merchantId = merchantId)
            if (staff == null || staff.position != "waiter") {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(staff)
        }
        get("all") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val waiters = StaffService.getAllWaiters(merchantId = merchantId, limit, offset)
            if(waiters.data?.isEmpty() == true){
                call.respond(HttpStatusCode.NoContent)
            }else{
                call.respond(HttpStatusCode.OK,waiters)
            }
        }
    }
}
