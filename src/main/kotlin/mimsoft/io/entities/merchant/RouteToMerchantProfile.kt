package mimsoft.io.entities.merchant

import io.ktor.server.routing.*
import mimsoft.io.entities.outcome.routeToFinance

fun Route.routeToMerchantProfile() {
    route("merchant") {
        routeToMerchantSettings()
        routeToFinance()
    }
}