package mimsoft.io.entities.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.payment.PaymentDto
import mimsoft.io.entities.payment.PaymentService
import mimsoft.io.entities.payment.SmsGatewayService

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