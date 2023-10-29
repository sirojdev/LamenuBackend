package mimsoft.io.integrate

import io.ktor.server.application.*
import io.ktor.server.routing.*
import mimsoft.io.features.payment.PaymentService

fun Route.paymentLinkRoute() {
  route("link") {
    get {
      val merchantId = call.parameters["appKey"]?.toLongOrNull()
      val paymentId = call.parameters["id"]?.toLongOrNull()
      val payment = PaymentService.getPaymentTypeClient(merchantId)
      if (paymentId?.toInt() == 2) {

      }
    }
  }
}
