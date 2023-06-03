package mimsoft.io.features.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.payment.PaymentDto

fun Route.routeToSmsGateways(){

    get("sms-gateway"){
        val merchantId = 2L
        val sms = SmsGatewayService.get(merchantId = merchantId)?: PaymentDto()
        call.respond(sms)
    }

    post ("sms-gateway"){
        val merchantId = 2L
        val table = call.receive<SmsGatewayDto>()
        SmsGatewayService.add(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

}