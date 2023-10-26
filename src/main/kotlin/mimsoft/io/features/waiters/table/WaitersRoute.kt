package mimsoft.io.features.waiters.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.waiter.WaiterService
import mimsoft.io.waiter.table.repository.WaiterTableRepository
import java.lang.Integer.min

fun Route.routeToWaiters() {
    val waiterTableRepository = WaiterTableRepository
    route("waiter") {

        get {
            val pr = getPrincipal()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 15, 30)
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val search = call.parameters["search"]
            val filter = call.parameters["filter"]
            val response = WaiterService.getAll(
                merchantId = merchantId,
                branchId = branchId,
                offset = offset,
                limit = limit,
                search = search,
                filter = filter
            )
            call.respond(response)
        }

        get("{id}") {
            val pr = getPrincipal()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            val response = waiterTableRepository.getWaiter(id = id, merchantId = merchantId, branchId = branchId)
            call.respond(response)
        }

        route("table") {
            put("join") {
                val pr = call.principal<BasePrincipal>()
                val tableId = call.parameters["tableId"]?.toLongOrNull()
                val waiterId = call.parameters["waiterId"]?.toLongOrNull()
                val merchantId = pr?.merchantId
                val rs = waiterTableRepository.joinToWaiter(waiterId, tableId, merchantId)
                if (rs != null) {
                    call.respond(HttpStatusCode.OK, rs)
                } else {
                    call.respond(HttpStatusCode.MethodNotAllowed)
                }
            }

            get("active") {
                val pr = call.principal<BasePrincipal>()
                val branchId = pr?.branchId
                val waiterId = call.parameters["waiterId"]?.toLong()
                val merchantId = pr?.merchantId
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val isExist = StaffService.isExist(waiterId, merchantId, branchId)
                if (isExist) {
                    val activeTables = waiterTableRepository.getActiveTablesWaiters(waiterId, limit, offset)
                    if (activeTables.data?.isEmpty() == true) {
                        call.respond(HttpStatusCode.NoContent)
                    }
                    call.respond(HttpStatusCode.OK, activeTables)
                } else {
                    call.respond(HttpStatusCode.MethodNotAllowed)
                }
            }

            get("finished") {
                val pr = call.principal<BasePrincipal>()
                val merchantId = pr?.merchantId
                val branchId = pr?.branchId
                val waiterId = call.parameters["waiterId"]?.toLong()
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val isExist = StaffService.isExist(waiterId, merchantId, branchId)
                if (isExist) {
                    val activeTables = waiterTableRepository.getFinishedTablesWaiters(waiterId, limit, offset)
                    if (activeTables.data?.isEmpty() == true) {
                        call.respond(HttpStatusCode.NoContent)
                    }
                    call.respond(HttpStatusCode.OK, activeTables)
                } else {
                    call.respond(HttpStatusCode.MethodNotAllowed)
                }
            }
        }
    }
}