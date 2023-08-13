package mimsoft.io.waiter.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.waiter.table.repository.WaiterTableRepository

fun Route.routeToWaitersTables() {
    val waterTableRepository = WaiterTableRepository
    route("table") {
        put("join") {
            val staffPrincipal = call.principal<StaffPrincipal>()
            val tableId = call.parameters["tableId"]?.toLong()
            val waiterId = staffPrincipal?.staffId
                val rs = waterTableRepository.joinToWaiter(waiterId, tableId)
                if(rs!=null){
                    call.respond(HttpStatusCode.OK,rs)
                }else{
                    call.respond(HttpStatusCode.MethodNotAllowed)
                }
        }
        get("active") {
            val staffPrincipal = call.principal<StaffPrincipal>()
            val waiterId = staffPrincipal?.staffId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val activeTables = waterTableRepository.getActiveTablesWaiters(waiterId,limit,offset)
            if (activeTables.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(HttpStatusCode.OK, activeTables)
        }
        get("finished") {
            val staffPrincipal = call.principal<StaffPrincipal>()
            val waiterId = staffPrincipal?.staffId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val activeTables = waterTableRepository.getFinishedTablesWaiters(waiterId,limit,offset)
            if (activeTables.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(HttpStatusCode.OK, activeTables)
        }
        put("finish"){
            val staffPrincipal = call.principal<StaffPrincipal>()
            val tableId = call.parameters["tableId"]?.toLong()
            val waiterId = staffPrincipal?.staffId
            val rs = waterTableRepository.finishTable(waiterId, tableId)
            if(rs){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}