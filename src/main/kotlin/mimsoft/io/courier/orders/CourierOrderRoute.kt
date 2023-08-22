package mimsoft.io.courier.orders

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCourierOrders() {

    val courierOrderService = CourierOrderService
    route("order") {
        get("") {
            val principal = call.principal<BasePrincipal>()
            val status = call.parameters["status"]
            if (status == null) {
                call.respond(ResponseModel(body = "status required"))
            }
            val orderList = OrderService.getAll(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "courierId" to principal.staffId,
                    "type" to call.parameters["type"] as Any,
                    "status" to call.parameters["status"] as Any,
                    "search" to call.parameters["search"] as Any,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any
                )
            )

            call.respond(orderList.httpStatus, orderList.body)
        }
        post("join") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<BasePrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.getOrderToCourier(
                courierId = courierId,
                orderId = orderId,
            )
            call.respond(result.httpStatus, result.body)
        }
        get("onway") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<BasePrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.toOnWay(
                courierId = courierId,
                orderId = orderId,
            )

            call.respond(result.httpStatus, result.body)

        }
        get("delivered") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<BasePrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.toDelivered(
                courierId = courierId,
                orderId = orderId,
            )

            call.respond(result.httpStatus, result.body)

        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            val order = OrderService.get(id)
            call.respond(order.httpStatus, order.body)
        }

    }
}