package mimsoft.io.features.menu

import io.ktor.server.routing.*

fun Route.routeToClient() {
    route("client") {
        routeToMenu()
    }
}