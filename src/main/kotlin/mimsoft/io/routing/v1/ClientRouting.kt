package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.client.branch.routeToClientBranches
import mimsoft.io.client.order.routeToOrderClient
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.routing.v1.client.routeToClientAuth
import mimsoft.io.client.menu.routeToMenu
import mimsoft.io.client.table.routeToClientTable
import mimsoft.io.features.book.routeToBook
import mimsoft.io.routing.v1.client.routeToClientDevice
import mimsoft.io.routing.v1.client.routeToClientProfile

fun Route.clientRouting() {

    route("client/auth") {
        routeToClientAuth()
    }
    authenticate("user") {
        route("client") {
            routeToClientDevice()
            routeToClientProfile()
            routeToMenu()
            routeToClientTable()
            routeToClientBranches()
            routeToAddress()
            routeToOrderClient()
            routeToBook()
        }
    }
}