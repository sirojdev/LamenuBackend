package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToOrder() {

    val repository: OrderRepository = OrderRepositoryImpl

    route("orders") {
        /**
         * OPERATOR ORDER NI QQABUL QILADI
         * */
        get("accepted") {
            val principal = call.principal<BasePrincipal>()
            val operatorId = principal?.staffId
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val merchantId = principal?.merchantId
            val rs = OrderRepositoryImpl.accepted(merchantId, orderId)
            val order = OrderMapper.toDto(OrderRepositoryImpl.getOrder(orderId))
            if (rs) {
                var offsett= 0
                OperatorSocketService.findNearCourierAndSendOrderToCourier(order,offsett)
                call.respond(rs)
            }else{
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }

        get("live") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val type = call.parameters["type"]
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orders = repository.getLiveOrders(type = type, limit = limit, offset = offset, merchantId = merchantId)
            val orderDto = orders?.data
            if (orderDto == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
        }

        get("all") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orders = repository.getAll(merchantId = merchantId, limit = limit, offset = offset)
            call.respond(orders)
        }

        get("history") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orders = repository.getAll(
                merchantId = merchantId,
                limit = limit,
                offset = offset,
                status = OrderStatus.CLOSED.name
            )
            if (orders.total == 0) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
        }

        get {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val search = call.parameters["search"]
            val status = call.parameters["status"]
            val type = call.parameters["type"]
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orders = repository.getAll(search, merchantId, status, type, limit, offset)
            if (orders.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
        }

        get("{id}") {
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

        post("/create") {
            val order = call.receive<OrderWrapper>()
            val status = repository.add(order)
            call.respond(status.httpStatus, status.body)
        }

        put {
            val order = call.receive<OrderDto>()
            repository.update(order)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
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

        put("update/details") {
            val detail = call.receive<OrderDetails>()
            if (detail.orderId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val response = repository.updateDetails(detail = detail)
            call.respond(response)
            return@put
        }
    }
}
