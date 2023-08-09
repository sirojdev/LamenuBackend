package mimsoft.io.integrate.onlinePbx

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.config.FORMATTER
import mimsoft.io.features.online_pbx.OnlinePbxServiceEntity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun Route.routeOnlinePbx() {

    val olinePbxService = OnlinePbxService
    val onlinePbxServiceEntity = OnlinePbxServiceEntity

    route("/onlinePbx") {

        post("hook") {
            val webhook = call.receiveParameters()

            call.respond(HttpStatusCode.OK)
            val event = webhook["event"]
            val direction = webhook["direction"]
            val caller = webhook["caller"]
            val callee = webhook["callee"]
            val domain = webhook["domain"]

            onlinePbxServiceEntity.get(domain)?.let {
                if (it == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@post
                }
            }

            OnlinePbxService.sendWebSocketMessage(PbxHookModel(event, direction, caller, callee))
            OnlinePbxService.saveHook(event, direction, caller, callee, webhook.toString())
        }

        get("callsHistory") {
            val startStampFrom = call.parameters["startStampFrom"]?.take(10)
            if (startStampFrom.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get

            }
            val start = SimpleDateFormat("yyyy-MM-dd").parse(startStampFrom).time / 1000
            val end = start + 86400

            val calls = OnlinePbxService.callHistories(start, end)
            if (calls != null) {
                call.respond(calls)
            }
        }
    }
}