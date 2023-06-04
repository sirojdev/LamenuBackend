package mimsoft.io.entities.client.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.entities.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.OrderDto
import mimsoft.io.utils.LaPrincipal

fun Route.routeToOrderClient() {

    val orderService: OrderRepository = OrderRepositoryImpl

    get("orders") {
        val principal = call.principal<LaPrincipal>()
        val orders = orderService.getByUserId(principal?.id)
        call.respond(orders.ifEmpty { HttpStatusCode.NoContent })
    }

    get("orders/{id}") {
        val principal = call.principal<LaPrincipal>()
        val id = call.parameters["id"]?.toLongOrNull()
        val order = orderService.get(id)
        if (order == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(order)
    }

    post("order") {
        val principal = call.principal<LaPrincipal>()
        val order = call.receive<OrderDto>()

        val id = orderService.add(order)
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        call.respond(HttpStatusCode.Created, id)
    }
}