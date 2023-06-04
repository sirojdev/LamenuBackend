package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.features.merchant.merchantRoute

fun Route.routeToAdmin()  {
    route("admin"){
        merchantRoute()
    }

}