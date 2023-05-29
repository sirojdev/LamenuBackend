package mimsoft.io.onlinePbx

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.text.get

fun Route.routeOnlinePbx() {
    route("/onlinePbx") {

        post("hook") {
            val webhook = call.receiveParameters()

            call.respond(HttpStatusCode.OK)
            val event = webhook["event"]
            val direction = webhook["direction"]
            val caller = webhook["caller"]
            val callee = webhook["callee"]
        }
    }
}