package mimsoft.io.entities.seles

import io.ktor.server.routing.*

fun Route.routeToMerchantProfile() {
    route("sales") {
        routeToMerchant()
    }
}