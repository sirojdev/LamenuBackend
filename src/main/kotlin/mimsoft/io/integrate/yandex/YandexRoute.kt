package mimsoft.io.integrate.yandex

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.yandex.module.YandexCheckPrice
import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.integrate.yandex.module.YandexTraffic
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToYandex() {
    route("yandex") {
        get("/tariff") {
            val dto = call.receive<YandexTraffic>()
            call.respond(YandexService.tariff(dto))
        }

        get("check-price") {
            val principal = call.receive<BasePrincipal>()
            val merchantId = principal.merchantId
            val item = call.receive<YandexCheckPrice>()
            call.respond(YandexService.checkPrice(item,merchantId))
        }
        post("create") {
            val item = call.receive<YandexOrder>()
            call.respond(YandexService.createOrder(item))
        }

        get() {
            YandexService.search()
        }
    }
}