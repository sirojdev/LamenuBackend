package mimsoft.io.waiter

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.visit.VisitService
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.waiter.auth.routeToWaiterAuth
import mimsoft.io.waiter.info.routeToWaitersInfo
import mimsoft.io.waiter.table.routeToWaitersTables

fun Route.routeToWaiter(){
    routeToWaiterAuth()
    authenticate("waiter") {
        route("waiter") {
            routeToWaitersInfo()
            routeToWaitersTables()

            put("verifyOrder/{id}"){
                val id = call.parameters["id"]?.toLongOrNull()
                VisitService.verifyOrder(id = id)
            }
        }
    }
}