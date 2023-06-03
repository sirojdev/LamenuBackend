package mimsoft.io.routing.admin

import io.ktor.server.routing.*
import mimsoft.io.features.merchant.merchantRoute

fun Route.routeToAdmin()  {
    route("admin"){
        merchantRoute()
    }

}