package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.client.branch.routeToClientBranches
import mimsoft.io.client.order.routeToOrderClient
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.client.menu.routeToMenu
import mimsoft.io.client.table.routeToClientTable

fun Route.clientRouting() {
    route("client") {
        routeToMenu()
        routeToClientTable()
        routeToClientBranches()
        routeToAddress()
        routeToOrderClient()
    }
}