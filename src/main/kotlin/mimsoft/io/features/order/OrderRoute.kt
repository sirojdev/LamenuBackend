package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.min
import mimsoft.io.board.socket.Action
import mimsoft.io.board.socket.BoardOrderStatus
import mimsoft.io.board.socket.BoardSocketService
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToOrder() {
  val orderService = OrderService

  route("orders") {
    route("admin") {
      get {
        val pr = getPrincipal()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val search = call.parameters["search"]
        val filter = call.parameters["filter"]
        val limit = min(call.parameters["limit"]?.toInt() ?: 10, 50)
        val offset = call.parameters["offset"]?.toInt() ?: 0
        val orders =
          OrderService.getForAdmin(
            merchantId = merchantId,
            branchId = branchId,
            search = search,
            filter = filter,
            limit = limit,
            offset = offset,
            statuses =
              """
                        (${OrderStatus.CANCELED.name})
                        (${OrderStatus.CLOSED.name})
                """
          )
        if (orders.data?.isEmpty() == true) {
          call.respond(HttpStatusCode.NoContent)
          return@get
        }
        call.respond(orders)
      }

      get("/{id}") {
        val pr = getPrincipal()
        val id = call.parameters["id"]?.toLongOrNull()
        val merchantId = pr?.merchantId
        val response =
          OrderService.get(
            id = id,
            merchantId = merchantId,
            "user",
            "products",
            "courier",
            "branch",
            "payment_type"
          )
        call.respond(response)
      }

      get("count") {
        val pr = getPrincipal()
        val response =
          OrderService.getOrderCountStatus(merchant = pr?.merchantId, branchId = pr?.branchId)
        if (response.isEmpty()) {
          call.respond(HttpStatusCode.NoContent)
          return@get
        }
        call.respond(response)
      }
    }

    put("status") {
      val pr = getPrincipal()
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val status = call.parameters["status"]
      if (status == null) {
        call.respond(
          ResponseModel(body = "status required", httpStatus = HttpStatusCode.BadRequest)
        )
      }
      val order = orderService.updateStatus(
          orderId = orderId,
          merchantId = pr?.merchantId,
          status = OrderStatus.valueOf(status.toString())
        )
      val st = OrderStatus.valueOf(status!!)
      if (order != null) {
        when (st) {
          OrderStatus.DONE -> {
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
          OrderStatus.ONWAVE,
          OrderStatus.COOKING -> {
            BoardSocketService.sendOrderToBoard(
              order = order,
              type = BoardOrderStatus.IN_PROGRESS,
              action = Action.UPDATE
            )
          }
          OrderStatus.ACCEPTED,
          OrderStatus.ONWAVE,
          OrderStatus.COOKING -> {
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
        if (order != null) {
          OperatorSocketService.findNearCourierAndSendOrderToCourier(order, offsett)
          BoardSocketService.sendOrderToBoard(order, BoardOrderStatus.IN_PROGRESS, Action.ADD)
        }
      } else {
        call.respond(HttpStatusCode.MethodNotAllowed)
      }
    }

    get("live") {
      val principal = getPrincipal()
      val merchantId = principal?.merchantId
      val branchId = principal?.branchId
      val search = call.parameters["search"]
      val filter = call.parameters["filter"]
      val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val response =
        orderService.getForAdmin(
          merchantId = merchantId,
          branchId = branchId,
          search = search,
          filter = filter,
          limit = limit,
          offset = offset,
          statuses =
            """
                    '${OrderStatus.ACCEPTED.name}',
                    '${OrderStatus.COOKING.name}',
                    '${OrderStatus.DONE.name}',
                    '${OrderStatus.ONWAVE.name}'
                """
              .trimIndent()
        )
      call.respond(response)
    }

    get {
      val principal = getPrincipal()
      val merchantId = principal?.merchantId
      val type = call.parameters["type"]
      val status = call.parameters["status"]
      val search = call.parameters["search"]
      val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0

      val response =
        orderService.getAll2(
          mapOf(
            "merchantId" to merchantId,
            "type" to type,
            "status" to status,
            "search" to search,
            "limit" to limit,
            "offset" to offset
          ),
          "user",
          "merchant",
          "branch",
          "order_price",
          "products",
          "collector",
          "courier",
          "payment_type"
        )
      call.respond(response.httpStatus, response.body)
    }

    get("{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }

      orderService.get(id).let { call.respond(it.httpStatus, it.body) }
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
      orderService.delete(id).let { call.respond(it.httpStatus, it.body) }
    }
  }
}
