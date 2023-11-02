package mimsoft.io.features.payment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToPayment() {
  get("payment") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val payment = PaymentService.get(merchantId = merchantId) ?: PaymentDto()
    call.respond(payment)
  }

  put("payment") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val table = call.receive<PaymentDto>()
    PaymentService.add(table.copy(merchantId = merchantId))
    call.respond(HttpStatusCode.OK)
  }
}
