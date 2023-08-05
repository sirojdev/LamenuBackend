package mimsoft.io.features.payment.payment_integration

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToPaymentIntegration(){
    val service = IntegrationService
    post("payment/integration") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val dto = call.receive<IntegrationDto>()
        val response = service.add(dto = dto.copy(merchantId = merchantId))
        call.respond(response)
        return@post
    }
}
