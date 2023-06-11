package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.features.merchant.merchantRoute
import mimsoft.io.features.payment_type.routeToPaymentType

fun Route.routeToAdmin()  {
    route("admin"){
        merchantRoute()
        routeToPaymentType()
    }

}