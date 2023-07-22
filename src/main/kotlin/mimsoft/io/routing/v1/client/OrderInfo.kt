package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl

fun Route.routeToClientOrderInfo() {
    val orderRepository: OrderRepository = OrderRepositoryImpl

    get("order/info/{id}") {
        val pr = call.principal<UserPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val get = orderRepository.get(id = id, merchantId = merchantId)
        if (get == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(get)
        return@get
    }
}