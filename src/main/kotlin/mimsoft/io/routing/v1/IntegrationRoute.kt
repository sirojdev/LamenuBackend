package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.integrate.jowi.routeToJowi
import mimsoft.io.integrate.click.routeToClick
import mimsoft.io.integrate.iiko.routeToIIko
import mimsoft.io.integrate.onlinePbx.routeOnlinePbx
import mimsoft.io.integrate.payme.routeToPayme
import mimsoft.io.integrate.yandex.routeToYandex

fun Route.routeToIntegration() {
    route("integration") {
        routeToPayme()
        routeToClick()
        routeOnlinePbx()
        routeToJowi()
        routeToIIko()
        routeToYandex()
    }
}