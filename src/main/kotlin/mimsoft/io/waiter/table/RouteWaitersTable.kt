package mimsoft.io.waiter.table

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.waiter.table.repository.WaiterTableRepository
import okhttp3.internal.wait

fun Route.routeToWaitersTables() {
    val waterTableRepository = WaiterTableRepository
    route("table") {
        put("table") {
            val staffPrincipal = call.principal<StaffPrincipal>()
            val tableId = call.parameters["tableId"]?.toLong()
            val waiterId = staffPrincipal?.staffId
            val isOPen = waterTableRepository.isOpenTable(tableId)
            if(!isOPen){
                call.respond(HttpStatusCode.MethodNotAllowed)
            }

            waterTableRepository.joinToWaiter(waiterId,tableId)


        }
    }
}