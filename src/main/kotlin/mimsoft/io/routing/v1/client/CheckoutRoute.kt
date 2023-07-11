package mimsoft.io.routing.v1.client

import io.ktor.server.routing.*
import mimsoft.io.features.checkout.routeToCheckout

fun Route.routeToClientCheckout(){
    routeToCheckout()
}