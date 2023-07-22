package mimsoft.io.integrate.click

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.click.ClickLogModel.Companion.COMPLETE_IN
import mimsoft.io.integrate.click.ClickLogModel.Companion.COMPLETE_OUT
import mimsoft.io.integrate.click.ClickLogModel.Companion.PREPARE_IN
import mimsoft.io.integrate.click.ClickLogModel.Companion.PREPARE_OUT

const val CLICK_EXPIRED_TIME = 45 * 60 * 1000L

fun Route.routeToClick() {

    post("payment/click/prepare/{merchantId}") {
        val merchantId = call.parameters["merchantId"]?.toLong()
        if (merchantId == null) {
            call.respondText("merchantId is null")
            return@post
        }
        val parameters = call.receiveParameters()
        ClickRepo.clickLog(PREPARE_IN, parameters)
        val response = ClickService.prepare(parameters, merchantId)
        ClickRepo.clickLog(PREPARE_OUT, response)
        call.respond(response)
    }

    post("payment/click/complete/{merchantId}") {
        val merchantId = call.parameters["merchantId"]?.toLong()
        if (merchantId == null) {
            call.respondText("merchantId is null")
            return@post
        }
        val parameters = call.receiveParameters()
        ClickRepo.clickLog(COMPLETE_IN, parameters)
        val response = ClickService.complete(parameters, merchantId)
        ClickRepo.clickLog(COMPLETE_OUT, response)
        call.respond(response)
    }

    get("payment/click/info") {
        call.respond(ClickRepo.clickLog())
    }
}