package mimsoft.io.features.sms

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.min
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToSms() {
  val smsService = SmsService

  get("sms") {
    val principal = call.principal<BasePrincipal>()
    val merchantId = principal?.merchantId
    val search = call.parameters["search"]
    val filters = call.parameters["filters"]
    val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
    val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
    val response =
      smsService.getAll(
        merchantId = merchantId,
        search = search,
        filters = filters,
        limit = limit,
        offset = offset,
      )
    if (response.data?.isNotEmpty() == true) {
      call.respond(response)
      return@get
    }
    call.respond(HttpStatusCode.NoContent)
  }

  post("sms") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val smsDto = call.receive<SmsDto>()
    val id = smsService.post(smsDto.copy(merchantId = merchantId))
    call.respond(id ?: HttpStatusCode.BadRequest)
  }

  delete("sms/{id}") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@delete
    }
    val result = smsService.delete(id = id, merchantId = merchantId)
    if (result) call.respond(result)
    call.respond(HttpStatusCode.NoContent)
  }

  get("sms/{id}") {
    val pr = call.principal<BasePrincipal>()
    val merchantId = pr?.merchantId
    val id = call.parameters["id"]?.toLongOrNull()
    if (id == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val sms = smsService.get(id = id, merchantId = merchantId)
    call.respond(sms ?: HttpStatusCode.NoContent)
  }
}
