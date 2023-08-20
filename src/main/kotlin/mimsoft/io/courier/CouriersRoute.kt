package mimsoft.io.courier

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.courier.auth.routeToCourierAuth
import mimsoft.io.courier.info.routeToCouriersInfo
import mimsoft.io.courier.orders.routeToCourierOrders
import mimsoft.io.courier.transaction.routeToCourierTransaction

fun Route.routeToCouriers() {
    routeToCourierAuth()
    authenticate("courier") {
        route("courier") {
            routeToCouriersInfo()
            routeToCourierTransaction()
            routeToCourierOrders()
        }

    }
}
