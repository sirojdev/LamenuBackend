package mimsoft.io.features.sms

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToSms() {
    val smsService = SmsService

    get("smss") {
        val smss = SmsService.getAll()
        call.respond(smss.ifEmpty { HttpStatusCode.NoContent })
    }

    get("sms/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val sms = SmsService.get(id)
        call.respond(sms ?: HttpStatusCode.NoContent)
    }

    post("sms") {
        val smsDto = call.receive<SmsDto>()
        val id = SmsService.post(smsDto)
        call.respond(id ?: HttpStatusCode.BadRequest)
    }

    delete("sms/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val result = SmsService.delete(id)
        call.respond(result)
    }
}