package mimsoft.io.features.seles

import io.ktor.server.routing.*

fun Route.routeToSales() {
    route("sales") {
        routeToMerchant()
    }
}