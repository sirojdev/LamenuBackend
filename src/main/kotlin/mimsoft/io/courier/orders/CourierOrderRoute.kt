package mimsoft.io.courier.orders

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.OrderStatus

fun Route.routeToCourierOrders() {
    val orderRepository = OrderRepositoryImpl
    val courierOrderService = CourierOrderService
    route("orders") {
        get("open") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orderList =
                courierOrderService.getOrdersBySomething(merchantId, OrderStatus.OPEN.name, null, limit, offset)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("active") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orderList = courierOrderService.getOrdersBySomething(
                merchantId,
                OrderStatus.ONWAY.name,
                courierId,
                limit,
                offset
            )
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("archive") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orderList = courierOrderService.getOrdersBySomething(
                merchantId,
                OrderStatus.DELIVERED.name,
                courierId,
                limit,
                offset
            )
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("accepted") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val orderList = courierOrderService.getAccepted(merchantId, OrderStatus.ACCEPTED.name, limit, offset)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("get") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.getOrderToCourier(
                courierId = courierId,
                orderId = orderId,
            )
            if (result == null) {
                call.respond(HttpStatusCode.MethodNotAllowed)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }
        get("onway") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.toOnWay(
                courierId = courierId,
                orderId = orderId,
            )
            if (result == null) {
                call.respond(HttpStatusCode.MethodNotAllowed)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }
        get("delivered") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val result = courierOrderService.toDelivered(
                courierId = courierId,
                orderId = orderId,
            )
            if (result == null) {
                call.respond(HttpStatusCode.MethodNotAllowed)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }
        get("/{id}") {
            val principal = call.principal<StaffPrincipal>()
            val id = call.parameters["id"]?.toLongOrNull()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val order = OrderRepositoryImpl.get(id, merchantId)
            if (order == null) {
                call.respond(HttpStatusCode.NotFound)
            }else if(order.order?.courier?.id!=courierId){
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
            call.respond(order)
        }

    }
}