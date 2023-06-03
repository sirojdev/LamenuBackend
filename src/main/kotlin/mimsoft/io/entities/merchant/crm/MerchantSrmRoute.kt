package mimsoft.io.entities.merchant.crm

import io.ktor.server.routing.*
import mimsoft.io.message.routeToMessage
import mimsoft.io.sms.routeToSms

fun Route.routeToCrm() {

    route("crm") {
        routeToSms()
        routeToMessage()

    }

}