package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseEnums
import mimsoft.io.services.firebase.FirebaseService
import mimsoft.io.utils.OrderStatus

fun Route.routeToClientDineIn() {
  post("call/waiter") {
    val waiterId = call.parameters["waiterId"]?.toLongOrNull()
    val tableId = call.parameters["tableId"]?.toLongOrNull()
    if (waiterId == null || tableId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }
    FirebaseService.callWaiterFromTable(waiterId = waiterId, tableId = tableId)
  }

  post("order") {
    val tableId = call.parameters["tableId"]?.toLongOrNull()
    val order = call.receive<Order>()
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val response =
      OrderService.post(
        order =
          order.copy(
            status = OrderStatus.OPEN,
            serviceType = BaseEnums.DINE_IN,
            merchant = MerchantDto(id = merchantId)
          )
      )
    FirebaseService.sendOrderToWaiter(tableId = tableId, dto = response)
  }
}
