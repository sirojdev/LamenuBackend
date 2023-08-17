package mimsoft.io.waiter

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.waiter.auth.routeToWaiterAuth
import mimsoft.io.waiter.info.routeToWaitersInfo
import mimsoft.io.waiter.table.routeToWaitersTables

fun Route.routeToWaiter(){
    routeToWaiterAuth()
    authenticate("waiter") {
        route("waiter") {
            routeToWaitersInfo()
            routeToWaitersTables()
        }

    }
}