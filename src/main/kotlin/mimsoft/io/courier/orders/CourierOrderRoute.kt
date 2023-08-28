package mimsoft.io.courier.orders

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.OrderStatus
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
            val orderList = OrderService.getAll2(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "courierId" to principal.staffId,
                    "type" to "DELIVERY",
                    "statuses" to listOf(
                        status.toString()
                    ),
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any
                ),
                "user","branch","payment_type"
            )

            call.respond(orderList.httpStatus, orderList.body)
        }
        get("open") {
            val principal = call.principal<BasePrincipal>()
            val orderList = OrderService.   getAll2(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "type" to "DELIVERY",
                    "statuses" to listOf(
                        OrderStatus.ACCEPTED.name
                    ),
                    "onWave" to false,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any
                ),
                "user","branch","payment_type"
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
            call.respond( courierOrderService.toOnWay(
                courierId = courierId,
                orderId = orderId,
            ))
        }
        get("delivered") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<BasePrincipal>()
            val courierId = principal?.staffId
            call.respond(courierOrderService.toDelivered(
                courierId = courierId,
                orderId = orderId,
            ))
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            val order = OrderService.get(id)
            call.respond(order.httpStatus, order.body)
        }
    }
}