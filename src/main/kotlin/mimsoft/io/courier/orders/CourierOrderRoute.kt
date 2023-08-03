package mimsoft.io.courier.orders

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.courier.CourierOrderService
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
            val orderList = courierOrderService.getOrdersBySomething(merchantId, OrderStatus.OPEN.name, null)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("active") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val orderList = courierOrderService.getOrdersBySomething(merchantId, OrderStatus.ONWAY.name, courierId)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("archive") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val orderList = courierOrderService.getOrdersBySomething(merchantId, OrderStatus.DELIVERED.name, courierId)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        get("accepted") {
            val principal = call.principal<StaffPrincipal>()
            val courierId = principal?.staffId
            val merchantId = principal?.merchantId
            val orderList = courierOrderService.getOrdersBySomething(merchantId, OrderStatus.ACCEPTED.name, null)
            if (orderList.isNullOrEmpty()) {
                call.respond(HttpStatusCode.NoContent)
            }
            call.respond(orderList)
        }
        put("get") {
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
        put("onway") {
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
        put("delivered") {
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
    }
}