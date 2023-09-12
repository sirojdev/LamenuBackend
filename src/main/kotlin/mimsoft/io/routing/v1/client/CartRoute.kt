package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.cart.CartService
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order

fun Route.routeToClientCart(){
    val cartService = CartService
    post("cart") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val dto = call.receive<Order>()
        val response = cartService.check(dto = dto.copy(merchant = MerchantDto(id = merchantId)))
        call.respond(response.httpStatus, response.body)
    }
}