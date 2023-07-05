package mimsoft.io.client.order

import ch.qos.logback.classic.db.names.ColumnName
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
import mimsoft.io.utils.ResponseModel

fun Route.routeToOrderClient() {
    val orderService: OrderRepository = OrderRepositoryImpl
    get("orders") {
        val userId = call.parameters["userId"]?.toLongOrNull()
        val principal = call.principal<LaPrincipal>()
        val orders = orderService.getBySomethingId(principal?.id?: userId)
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
        return@get
    }

    post("order") {
        val principal = call.principal<LaPrincipal>()
        val order = call.receive<OrderWrapper>()

        val status = orderService.add(order)

        call.respond(
            status?.httpStatus?: ResponseModel.SOME_THING_WRONG,
            status?.body?:
            status?.httpStatus?.description?:
            ResponseModel.SOME_THING_WRONG.description)
    }

    delete("order/{id}") {
        val principal = call.principal<LaPrincipal>()
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(
            status?.httpStatus?: ResponseModel.SOME_THING_WRONG,
            status?.body?: status?.httpStatus?.description?: "Something went wrong")

    }
}