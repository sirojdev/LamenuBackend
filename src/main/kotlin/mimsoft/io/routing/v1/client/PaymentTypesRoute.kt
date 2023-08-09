package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.payment.PaymentService

fun Route.routeToPaymentTypes() {
    get("payment/types") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        val respond = PaymentService.getPaymentTypeClient(merchantId = merchantId)
        call.respond(respond)
        return@get
    }
}