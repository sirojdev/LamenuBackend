package mimsoft.io.features.message

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToMessage() {

    val messageService = MessageService

    get("messages") {
        val messages = messageService.getAll()
        call.respond(messages.ifEmpty { HttpStatusCode.NoContent })
    }

    get("message/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val message = messageService.get(id)
        call.respond(message ?: HttpStatusCode.NoContent)
    }

    post("message") {
        val messageDto = call.receive<MessageDto>()
        val id = messageService.post(messageDto)
        call.respond(id ?: HttpStatusCode.BadRequest)
    }

    delete("message/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val result = messageService.delete(id)
        call.respond(result)
    }
}