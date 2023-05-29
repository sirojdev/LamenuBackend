package mimsoft.io.entities.merchant

import io.ktor.server.routing.*

fun Route.routeToMerchantProfile() {
    route("merchant") {
        routeToMerchantSettings()
    }
}