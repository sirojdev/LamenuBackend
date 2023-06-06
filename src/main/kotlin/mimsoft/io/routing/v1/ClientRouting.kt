package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.client.order.routeToOrderClient
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.features.menu.routeToMenu

fun Route.clientRouting() {
    route("client") {
        routeToMenu()
        routeToAddress()
        routeToOrderClient()
    }
}