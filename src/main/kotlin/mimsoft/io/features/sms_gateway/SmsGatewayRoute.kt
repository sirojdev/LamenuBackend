package mimsoft.io.features.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToSmsGateways() {

    get("sms-gateway") {
        val merchantId = 1L
        val sms = SmsGatewayService.get(merchantId = merchantId) ?: SmsGatewayDto()
        call.respond(sms)
    }

    put("sms-gateway") {
        val merchantId = 1L
        val table = call.receive<SmsGatewayDto>()
        SmsGatewayService.update(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

}