package mimsoft.io.features.checkout

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.OrderModel

fun Route.routeToCheckout() {
    post("order/checkout") {
        val dto = call.receive<OrderModel>()
        val response = CheckoutService.calculate(dto = dto)
        call.respond(response)
    }
}