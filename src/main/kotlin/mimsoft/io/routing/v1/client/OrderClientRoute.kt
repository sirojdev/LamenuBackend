package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.ArrayList
import kotlin.math.min
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderRateModel
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.plugins.getPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToClientOrder() {
  val orderService = OrderService
  val log: Logger = LoggerFactory.getLogger("routeToClientOrder")
  get("orders") {
    val pr = getPrincipal()
    val statuses = call.parameters["statuses"]
    val s = statuses?.split(",")?.iterator()
    val list = ArrayList<String>()
    if (s != null) {
      s.forEach { list.add(it) }
    }
    val search = call.parameters["search"]
    val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
    val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
    log.info("search {}", search)
    val response =
      OrderService.getAll2(
        params =
          mapOf(
            "userId" to pr?.userId,
            "merchantId" to pr?.merchantId,
            "search" to search,
            "statuses" to list,
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
    return@get
  }

  get("order/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    val order = orderService.get(id)
    call.respond(order.httpStatus, order.body)
  }

  post("order/create") {
    val pr = getPrincipal()
    val userId = pr?.userId
    val merchantId = pr?.merchantId
    val order = call.receive<Order>()
    val status =
      orderService.post(
        order.copy(user = UserDto(id = userId), merchant = MerchantDto(id = merchantId))
      )
    if (status.httpStatus== HttpStatusCode.OK)OperatorSocketService.sendOrdersToOperators(status.body as Order)
    call.respond(status.httpStatus, status.body)
  }

  delete("order/{id}") {
    val id = call.parameters["id"]?.toLongOrNull()
    val status = orderService.delete(id)
    call.respond(status.httpStatus, status.body)
  }

  put("order/rate") {
    val pr = getPrincipal()
    val userId = pr?.userId
    val rate = call.receive<OrderRateModel>()
    val response = orderService.orderRate(rate = rate.copy(userId = userId))
    call.respond(response)
  }
}
