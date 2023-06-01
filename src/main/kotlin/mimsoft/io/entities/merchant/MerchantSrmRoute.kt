package mimsoft.io.entities.merchant

import io.ktor.server.routing.*
import mimsoft.io.message.routeToMessage
import mimsoft.io.sms.routeToSms

fun Route.routeToSrm() {
    route("srm") {

        routeToSms()
        routeToMessage()
    }
}