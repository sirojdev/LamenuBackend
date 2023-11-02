package mimsoft.io.integrate.click

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mimsoft.io.integrate.click.ClickLogModel.Companion.COMPLETE_IN
import mimsoft.io.integrate.click.ClickLogModel.Companion.COMPLETE_OUT
import mimsoft.io.integrate.click.ClickLogModel.Companion.PREPARE_IN
import mimsoft.io.integrate.click.ClickLogModel.Companion.PREPARE_OUT
import mimsoft.io.utils.toJson

const val CLICK_EXPIRED_TIME = 45 * 60 * 1000L

fun Route.routeToClick() {

  post("payment/click/prepare/{merchantId}") {
    println("INSIDE POST PREPARE")
    val merchantId = call.parameters["merchantId"]?.toLong()
    if (merchantId == null) {
      call.respondText("merchantId is null")
      return@post
    }
    val parameters = call.receiveParameters()
    println("PARAMETRES ${parameters.toJson()}")
    val response = ClickService.prepare(parameters, merchantId)
    coroutineScope {
      launch {
        ClickRepo.clickLog(PREPARE_IN, parameters)
        ClickRepo.clickLog(PREPARE_OUT, response)
      }
    }
    println(" finish $response")
    call.respond(response)
  }

  post("payment/click/complete/{merchantId}") {
    val merchantId = call.parameters["merchantId"]?.toLong()
    if (merchantId == null) {
      call.respondText("merchantId is null")
      return@post
    }
    val parameters = call.receiveParameters()
    println(parameters)
    println(parameters["click_trans_id"])
    val response = ClickService.complete(parameters, merchantId)
    coroutineScope {
      launch {
        ClickRepo.clickLog(COMPLETE_IN, parameters)
        ClickRepo.clickLog(COMPLETE_OUT, response)
      }
    }
    println("response complete $response")
    call.respond(response)
  }

  get("payment/click/info") { call.respond(ClickRepo.clickLog()) }

  get("payment/click/{merchantId}/{orderId}/{amount}") {
    val merchantId = call.parameters["merchantId"]?.toLongOrNull()
    val amount = call.parameters["amount"]?.toLong()
    val orderId = call.parameters["orderId"]?.toLongOrNull()
    if (merchantId == null || amount == null || orderId == null) {
      call.respondText("merchantId or amount or orderId is null")
      return@get
    }
    val response = ClickService.getCheckout(orderId, amount, merchantId)
    call.respond(response)
  }
}
