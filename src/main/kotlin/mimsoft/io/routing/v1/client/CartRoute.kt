package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.cart.CartInfoDto
import mimsoft.io.features.cart.CartService

fun Route.routeToClientCart(){
    val cartService = CartService
    post("cart") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val dto = call.receive<CartInfoDto>()
        val response = cartService.check(dto = dto, merchantId = merchantId)
        if(response == null){
            call.respond(HttpStatusCode.NoContent)
            return@post
        }
        call.respond(response)
        return@post
    }
}