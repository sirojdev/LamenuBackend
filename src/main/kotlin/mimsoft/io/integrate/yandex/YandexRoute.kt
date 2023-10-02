package mimsoft.io.integrate.yandex

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.yandex.module.YandexCheckPrice
import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToYandex() {
    route("yandex") {
        get("/tariff") {
            val branchId = call.parameters["branchId"]?.toLongOrNull()
//            val principal = call.principal<BasePrincipal>()
//            val merchantId = principal?.merchantId
            val merchantId = 1L
            call.respond(YandexService.tariff(branchId = branchId, merchantId = merchantId))
        }

        get("check-price") {
            val item = call.receive<YandexCheckPrice>()
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            val principal = call.principal<BasePrincipal>()
            val merchantId = 1L
            call.respond(YandexService.checkPrice(item, merchantId, orderId))
        }
        post("create") {
            val item = call.receive<YandexOrder>()
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            call.respond(YandexService.createOrder(item, orderId))
        }
        post("confirm") {
            val orderId = call.parameters["orderId"]?.toLongOrNull()
            YandexService.confirm(orderId = orderId, merchantId = 1)
        }

        get() {
            YandexService.search()
        }

        post("callback") {
            val myOrderId = call.parameters["my_order_id"]
            val claimId = call.parameters["claim_id"]
            println("my order id  = $myOrderId")
            println("my claim id  = $claimId")
        }
    }
}