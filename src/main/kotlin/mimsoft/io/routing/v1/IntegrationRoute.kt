package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.integrate.click.routeToClick
import mimsoft.io.integrate.payme.routeToPayme

fun Route.routeToIntegration() {
    route("integration") {
        routeToPayme()
        routeToClick()
    }
}