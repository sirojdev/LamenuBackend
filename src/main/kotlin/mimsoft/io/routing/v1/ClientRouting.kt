package mimsoft.io.routing.client

import io.ktor.server.routing.*
import mimsoft.io.features.address.routeToAddress
import mimsoft.io.features.menu.routeToMenu

fun Route.clientRouting() {
    route("client") {
        routeToMenu()
        routeToAddress()
    }
}