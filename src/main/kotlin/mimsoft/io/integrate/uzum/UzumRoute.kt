package mimsoft.io.integrate.uzum

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToUzum() {
    post("register") {
        val orderId = call.parameters["orderId"]?.toLongOrNull()
        if (orderId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
         UzumService.register(orderId)
    }
}