package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.utils.plugins.getPrincipal


fun Route.routeToOrder() {

    val orderService = OrderService

    route("orders") {

        /**
         * OPERATOR ORDER NI QABUL QILADI
         * */
        get("/accepted") {
            val principal = getPrincipal()
            val operatorId = principal?.staffId
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val merchantId = principal?.merchantId
            val rs = orderService.accepted(merchantId, orderId)
            val order = OrderService.get(orderId).body as Order
            if (rs) {
                var offsett = 0
                OperatorSocketService.findNearCourierAndSendOrderToCourier(order, offsett)
                call.respond(rs)
            } else {
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }

        get("/live") {
            val principal = getPrincipal()
            val response = orderService.getAll(
                params = mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any,
                    "type" to call.parameters["type"] as Any
                )
            )
            call.respond(response.httpStatus, response.body)
        }

        get {
            val principal = getPrincipal()
            val response = orderService.getAll(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "type" to call.parameters["type"] as Any,
                    "status" to call.parameters["status"] as Any,
                    "search" to call.parameters["search"] as Any,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any
                )
            )
            call.respond(response.httpStatus, response.body)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            orderService.get(id).let {
                call.respond(it.httpStatus, it.body)
            }
        }

        post("/create") {
            val principal = getPrincipal()
            val order = call.receive<Order>()
            orderService.post(order.copy(merchant = MerchantDto(id = principal?.merchantId))).let {
                call.respond(it.httpStatus, it.body)
            }
        }

        delete("/id") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            orderService.delete(id).let {
                call.respond(it.httpStatus, it.body)
            }
        }
    }

}

