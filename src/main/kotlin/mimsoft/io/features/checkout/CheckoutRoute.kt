package mimsoft.io.features.checkout

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.OrderModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToCheckout() {
    val log: Logger = LoggerFactory.getLogger("CheckoutRoute")
    post("order/checkout") {
        val dto = call.receive<OrderModel>()
        log.info("CheckoutRoute: $dto")
        val response = CheckoutService.calculate(dto = dto)
        call.respond(response)
    }
}