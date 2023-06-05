package mimsoft.io.features.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.services.sms.SmsSenderService

fun Route.routeToSmsGateways() {

    val smsSenderService = SmsSenderService

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

    post("sms-gateway-test") {
        val merchantId = 1L
        val phone = call.receive<Phone01>()
        println("\nphone-->$phone")
        val status = smsSenderService.send(merchantId = merchantId, phone = phone.phone.toString(), "test message")
        println("\nstatus-->$status")
        if (status == "DONE") {
            call.respond(HttpStatusCode.OK)
            return@post
        }
        call.respond(HttpStatusCode.Unauthorized)
    }

}

data class Phone01(
    val phone: String? = null
)