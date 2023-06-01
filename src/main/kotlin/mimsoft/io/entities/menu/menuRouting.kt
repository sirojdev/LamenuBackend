package mimsoft.io.entities.menu

import io.ktor.server.routing.*

fun Route.routeToClient() {
    route("client") {
        routeToMenu()
    }
}