package mimsoft.io.courier

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import mimsoft.io.courier.auth.routeToCourierAuth
import mimsoft.io.courier.info.routeToCouriersInfo
import mimsoft.io.courier.merchantChat.merchantChatRoute
import mimsoft.io.courier.orders.routeToCourierOrders
import mimsoft.io.courier.transaction.routeToCourierTransaction
import mimsoft.io.features.operator.socket.OrderCourierDto
import mimsoft.io.features.order.OrderService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType

fun Route.routeToCouriers() {
  post("test") {
    val params = call.parameters["orderId"]?.toLong()
    val order = OrderService.getById(params, "user", "branch", "address")
    val orderCourierDto =
      OrderCourierDto(
        id = order?.id,
        address = order?.address?.description,
        branchName = order?.branch?.name,
        clientFirst = order?.user?.firstName,
        clientLastName = order?.user?.lastName,
        price = order?.totalPrice
      )
    val socketDto = SocketData(data = Gson().toJson(orderCourierDto), type = SocketType.ORDER)
    println("size ${CourierSocketService.courierConnections.size}")
    CourierSocketService.courierConnections.forEach { x ->
      x.session?.send(Gson().toJson(socketDto))
    }
    call.respond(HttpStatusCode.OK)
  }
  routeToCourierAuth()
  authenticate("courier") {
    route("courier") {
      routeToCouriersInfo()
      routeToCourierTransaction()
      routeToCourierOrders()
      merchantChatRoute()
    }
  }
}
