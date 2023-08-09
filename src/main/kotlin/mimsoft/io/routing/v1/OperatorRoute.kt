package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.routeToCourier
import mimsoft.io.features.message.routeToMessage
import mimsoft.io.features.notification.routeToNotification
import mimsoft.io.features.operator.routeToOperatorEntity
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.promo.routeToPromo
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.routing.merchant.routeToUserUser

fun Route.routeToOperator() {

    routeToOperatorEntity()

    route("operator") {

        authenticate("operator") {

            routeToSms()
            routeToMessage()
            routeToPromo()
            routeToUserUser()
            routeToNotification()

            routeToOrder()
            routeToCourier()
            routeToCollector()
        }
    }

}