package mimsoft.io.integrate.yandex

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToYandex() {
    get("/yandex") {
        call.respondText("Yandex")
    }
}