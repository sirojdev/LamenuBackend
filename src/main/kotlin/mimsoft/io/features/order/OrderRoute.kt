package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToOrder() {

    val repository: OrderRepository = OrderRepositoryImpl

    route("orders") {
        get("live") {
            val type = call.parameters["type"]
            val orders = repository.getLiveOrders(type = type.toString())
            val orderDto = orders?.data
            if (orderDto == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orderDto)
        }
        get("all") {

            val orders = repository.getAll()
            call.respond(orders)
        }
        get("live") {
            val type = call.parameters["type"]
            val orders = repository.getLiveOrders(type = type.toString())
            val orderDto = orders?.data
            if (orderDto == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
        }

        get("history") {
            val orders = repository.getAll()
            if (orders.total == 0) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
        }

        get("/orders") {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val search = call.parameters["search"]
            val status = call.parameters["status"]
            val type = call.parameters["type"]
            val limit = call.parameters["limit"]?.toIntOrNull()
            val offset = call.parameters["offset"]?.toIntOrNull()
            val orders = repository.getAll(search, merchantId, status, type, limit, offset)?.data
            if (orders == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
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
            val order = call.receive<OrderWrapper>()
            val status = repository.add(order)
            call.respond(
                status?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
                status?.body ?: "Something went wrong"
            )
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
                call.respond(
                    deleted?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
                    deleted?.body ?: "Something went wrong"
                )
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
