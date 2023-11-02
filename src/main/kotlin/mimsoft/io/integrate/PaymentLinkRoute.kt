package mimsoft.io.integrate

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.order.OrderService
import mimsoft.io.integrate.click.ClickService
import mimsoft.io.integrate.payme.PaymeService
import mimsoft.io.integrate.uzum.UzumService

fun Route.paymentLinkRoute() {
  route("payment/link") {
    get {
      val merchantId = call.parameters["appKey"]?.toLongOrNull()
      val orderId = call.parameters["orderId"]?.toLong()
      val order = OrderService.getById(orderId, "payment_type")
      when (order?.paymentMethod?.id) {
        2L -> {
          call.respond(ClickService.getCheckout(orderId!!, order.totalPrice!!, merchantId).link!!)
        }
        3L -> {
          call.respond(PaymeService.getCheckout(orderId!!, order.totalPrice!!, merchantId).link!!)
        }
        7L -> {
          call.respond(UzumService.register(orderId!!))
        }
      }
    }
  }
}
