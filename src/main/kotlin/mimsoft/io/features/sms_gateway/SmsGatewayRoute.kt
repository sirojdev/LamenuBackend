package mimsoft.io.features.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.services.sms.SmsSenderService
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToSmsGateways() {

  val smsSenderService = SmsSenderService

  get("sms-gateway") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val sms = SmsGatewayService.get(merchantId = merchantId) ?: SmsGatewayDto()
    call.respond(sms)
  }

  put("sms-gateway") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val table = call.receive<SmsGatewayDto>()
    SmsGatewayService.add(table.copy(merchantId = merchantId))
    call.respond(HttpStatusCode.OK)
  }

  post("sms-gateway-test") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val phone = call.receive<Phone01>()
    val status =
      smsSenderService.send(merchantId = merchantId, phone = phone.phone.toString(), "test message")
    if (status == "DONE") {
      call.respond(HttpStatusCode.OK)
      return@post
    }
    call.respond(HttpStatusCode.Unauthorized)
  }
}

data class Phone01(val phone: String? = null)
