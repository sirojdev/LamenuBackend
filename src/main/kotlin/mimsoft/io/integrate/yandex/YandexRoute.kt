package mimsoft.io.integrate.yandex

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.integrate.yandex.module.Item
import mimsoft.io.integrate.yandex.module.YandexCheckPrice
import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.integrate.yandex.module.YandexTraffic

fun Route.routeToYandex() {
    route("yandex") {
        get("/tariff") {
            val dto = call.receive<YandexTraffic>()
            call.respond(YandexService.tariff(dto))
        }

        get("check-price") {
          val item = call.receive<YandexCheckPrice>()
            call.respond(YandexService.checkPrice(item))
        }
        post("create"){
            val item = call.receive<YandexOrder>()
            call.respond(YandexService.createOrder(item))
        }

        get(){
            YandexService.search()
        }
    }
}