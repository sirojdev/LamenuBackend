package mimsoft.io.admin

import io.ktor.server.routing.*
import mimsoft.io.entities.merchant.routeToMerchant

fun Route.routeToAdmin()  {
    route("admin"){
        routeToMerchant()
    }

}