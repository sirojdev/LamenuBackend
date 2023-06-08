package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToOrder() {

    val repository: OrderRepository = OrderRepositoryImpl
    val mapper = OrderMapper

    get("live") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val type = call.parameters["type"]
        val orders = repository.getLiveOrders(type = type.toString(), merchantId = merchantId)
        val orderDto = orders?.data
        if(orderDto == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, orderDto)
    }

    get("history") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val orders = repository.getAll(merchantId=merchantId)
        if(orders == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, orders)
    }

    get("/orders") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val status = call.parameters["status"]
        val type = call.parameters["type"]
        val limit = call.parameters["limit"]?.toIntOrNull()
        val offset = call.parameters["offset"]?.toIntOrNull()
        val orders = repository.getAll(merchantId = merchantId, status, type, limit, offset)?.data
        if (orders == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond (HttpStatusCode.OK, orders)
    }

    get("/order/{id}") {

        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val order = repository.get(id)
        if (order != null) {
            call.respond(HttpStatusCode.OK, order)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/order/create") {
        val order = call.receive<OrderDto>()
        val id = repository.add(order)
        call.respond(HttpStatusCode.OK, OrderId(id))
    }

    put("/order") {
        val order = call.receive<OrderDto>()
        repository.update(order)
        call.respond(HttpStatusCode.OK)
    }

    delete("/order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = repository.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

data class OrderId(
    val id: Long? = null
)