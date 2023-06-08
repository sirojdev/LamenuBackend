package mimsoft.io.client.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.LaPrincipal
import mimsoft.io.utils.SOME_THING_WRONG

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
        val order = call.receive<OrderWrapper>()

        val status = orderService.add(order)

        call.respond(
            status?.httpStatus?: SOME_THING_WRONG,
            status?.body?:
            status?.httpStatus?.description?:
            SOME_THING_WRONG.description)
    }

    delete("order/{id}") {
        val principal = call.principal<LaPrincipal>()
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(
            status?.httpStatus?: SOME_THING_WRONG,
            status?.body?: "Something went wrong")

    }
}