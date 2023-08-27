package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.appKey.routeToMerchantApp
import mimsoft.io.routing.v1.admin.merchantRoute
import mimsoft.io.features.payment_type.routeToPaymentType

fun Route.routeToAdmin()  {
    route("admin"){
        merchantRoute()
        routeToMerchantApp()
        authenticate ("merchant"){
            routeToPaymentType()
        }
    }

}