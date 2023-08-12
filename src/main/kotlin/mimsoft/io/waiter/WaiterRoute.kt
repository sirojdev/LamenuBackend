package mimsoft.io.waiter

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.courier.auth.routeToCourierAuth
import mimsoft.io.courier.info.routeToCouriersInfo
import mimsoft.io.courier.orders.routeToCourierOrders
import mimsoft.io.courier.transaction.routeToCourierTransaction
import mimsoft.io.waiter.auth.routeToWaiterAuth
import mimsoft.io.waiter.info.routeToWaitersInfo
import mimsoft.io.waiter.table.routeToCourierTables

fun Route.routeToWaiter(){
    routeToWaiterAuth()
    authenticate("staff") {
        route("courier") {
            routeToWaitersInfo()
            routeToCourierTables()
        }

    }
}