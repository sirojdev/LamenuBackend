package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.board.socket.Action
import mimsoft.io.board.socket.BoardOrderStatus
import mimsoft.io.board.socket.BoardSocketService
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.firebase.FirebaseService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal
import kotlin.math.min

fun Route.routeToOrder() {
    val orderService = OrderService

    route("orders") {
        get("status") {
            val pr = getPrincipal()
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val status = call.parameters["status"]
            if (status == null) {
                call.respond(ResponseModel(body = "status required", httpStatus = HttpStatusCode.BadRequest))
            }
            val order = orderService.updateStatus(
                orderId = orderId,
                merchantId = pr?.merchantId,
                status = OrderStatus.valueOf(status.toString())
            )
            val st = OrderStatus.valueOf(status!!)
            if (order != null) {
                when (st) {
                    OrderStatus.READY -> {
                        BoardSocketService.sendOrderToBoard(
                            order = order,
                            type = BoardOrderStatus.READY,
                            action = Action.ADD
                        )
                    }

                    OrderStatus.ONWAY -> {
                        BoardSocketService.sendOrderToBoard(
                            order = order,
                            type = BoardOrderStatus.READY,
                            action = Action.REMOVE
                        )
                    }

                    OrderStatus.ONWAVE, OrderStatus.COOKING -> {
                        BoardSocketService.sendOrderToBoard(
                            order = order,
                            type = BoardOrderStatus.IN_PROGRESS,
                            action = Action.UPDATE
                        )
                    }

                    OrderStatus.ACCEPTED, OrderStatus.ONWAVE, OrderStatus.COOKING -> {
                        BoardSocketService.sendOrderToBoard(
                            order = order,
                            type = BoardOrderStatus.IN_PROGRESS,
                            action = Action.ADD
                        )
                    }

                    else -> {}
                }
            }
            call.respond(order!!)
        }

        get("accepted") {
            val principal = getPrincipal()
            val operatorId = principal?.staffId
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val merchantId = principal?.merchantId
            val rs = orderService.accepted(merchantId, orderId)
            if (rs) {
                call.respond(rs)
//                FirebaseService.sendNotificationOrderToClient(order)
                val offsett = 0
                val order = OrderService.getById(orderId, "user", "branch", "address")
                if (order!=null){
                    OperatorSocketService.findNearCourierAndSendOrderToCourier(order, offsett)
                    BoardSocketService.sendOrderToBoard(order, BoardOrderStatus.IN_PROGRESS, Action.ADD)
                }
            } else {
                call.respond(HttpStatusCode.MethodNotAllowed)
            }
        }

        get("live") {
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
            val merchantId = principal?.merchantId
            val type = call.parameters["type"]
            val status = call.parameters["status"]
            val search = call.parameters["search"]
            val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0

            val response = orderService.getAll2(
                mapOf(
                    "merchantId" to merchantId,
                    "type" to type,
                    "status" to status,
                    "search" to search,
                    "limit" to limit,
                    "offset" to offset
                ),
                "user", "merchant", "branch", "order_price", "products", "collector", "courier", "payment_type"
            )
            call.respond(response.httpStatus, response.body)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            orderService.get(id).let {
                call.respond(it.httpStatus, it.body)
            }
        }

        post("create") {
            val principal = getPrincipal()
            val order = call.receive<Order>()
            orderService.post(order.copy(merchant = MerchantDto(id = principal?.merchantId))).let {
                call.respond(it.httpStatus, it.body)
            }
        }

        delete("{id}") {
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

