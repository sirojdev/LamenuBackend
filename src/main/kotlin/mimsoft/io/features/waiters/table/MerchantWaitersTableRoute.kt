package mimsoft.io.features.waiters.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal
import mimsoft.io.waiter.table.repository.WaiterTableRepository

fun Route.routeToMerchantWaitersTable() {
    route("table") {
        val waterTableRepository = WaiterTableRepository
        put("join") {
            val merchantPrincipal = call.principal<MerchantPrincipal>()
            val tableId = call.parameters["tableId"]?.toLong()
            val waiterId = call.parameters["waiterId"]?.toLong()
            val merchantId = merchantPrincipal?.merchantId
            val rs = waterTableRepository.joinToWaiter(waiterId, tableId, merchantId)
            if (rs != null) {
                call.respond(HttpStatusCode.OK, rs)
            } else {
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }
        get("active") {
            val merchantPrincipal = call.principal<MerchantPrincipal>()
            val waiterId = call.parameters["waiterId"]?.toLong()
            val merchantId = merchantPrincipal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val isExist = StaffService.isExist(waiterId, merchantId)
            if(isExist){
                val activeTables = waterTableRepository.getActiveTablesWaiters(waiterId, limit, offset)
                if (activeTables.data?.isEmpty() == true) {
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK, activeTables)
            }else{
                call.respond(HttpStatusCode.MethodNotAllowed)
            }

        }
        get("finished") {
            val merchantPrincipal = call.principal<MerchantPrincipal>()
            val merchantId = merchantPrincipal?.merchantId
            val waiterId = call.parameters["waiterId"]?.toLong()
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val isExist = StaffService.isExist(waiterId, merchantId)
            if(isExist){
                val activeTables = waterTableRepository.getFinishedTablesWaiters(waiterId, limit, offset)
                if (activeTables.data?.isEmpty() == true) {
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK, activeTables)
            }else{
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }

    }
}