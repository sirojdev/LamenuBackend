package mimsoft.io.integrate.click

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val CLICK_EXPIRED_TIME = 45 * 60 * 1000L

fun Route.routeToClick() {

    post("payment/click/prepare/{merchantId}") {
        val merchantId = call.parameters["merchantId"]?.toLong()
        if (merchantId == null) {
            call.respondText("merchantId is null")
            return@post
        }
        val parameters = call.receiveParameters()
    }
}