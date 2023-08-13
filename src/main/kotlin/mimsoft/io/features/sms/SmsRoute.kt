package mimsoft.io.features.sms

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToSms() {
    val smsService = SmsService

    get("sms") {
        val principal = call.principal<BasePrincipal>()
        val merchantId = principal?.merchantId
        val limit = call.parameters["limit"]?.toIntOrNull()
        val offset = call.parameters["offset"]?.toIntOrNull()
        if (limit != null && offset != null) {
            val sms = smsService.getAll(merchantId = merchantId, limit = limit, offset = offset)
            call.respond(sms?: HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.BadRequest)
        return@get
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
        call.respond(result)
    }

}