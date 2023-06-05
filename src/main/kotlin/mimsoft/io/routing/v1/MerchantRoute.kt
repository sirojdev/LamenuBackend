package mimsoft.io.routing.v1

import io.ktor.server.routing.*
import mimsoft.io.entities.client.user.routeToUser
import mimsoft.io.features.app.routeToApp
import mimsoft.io.features.badge.routeToBadge
import mimsoft.io.features.branch.routeToBranch
import mimsoft.io.features.category.routeToCategory
import mimsoft.io.features.delivery.routeToDelivery
import mimsoft.io.features.extra.routeToExtra
import mimsoft.io.features.flat.routeToFlat
import mimsoft.io.features.label.routeToLabel
import mimsoft.io.features.message.routeToMessage
import mimsoft.io.features.option.routeToOption
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.outcome.routeToOutcome
import mimsoft.io.features.outcome_type.outcomeTypeRoute
import mimsoft.io.features.payment.routeToPayment
import mimsoft.io.features.poster.routeToPoster
import mimsoft.io.features.product.routeToProduct
import mimsoft.io.features.room.routeToRoom
import mimsoft.io.features.sms_gateway.routeToSmsGateways
import mimsoft.io.features.staff.routeToStaff
import mimsoft.io.features.table.routeToTable
import mimsoft.io.features.telephony.routeToTelephony
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.telegram_bot.routeToBot


fun Route.routeToMerchant() {

    route("merchant") {

        route("settings") {
            routeToStaff()
            routeToBranch()
            routeToCategory()
            routeToExtra()
            routeToLabel()
            routeToOption()
            routeToProduct()
            routeToOrder()
            routeToTable()
            routeToRoom()
            routeToFlat()
            routeToBot()
            routeToPoster()
            routeToApp()
            routeToPayment()
            routeToSmsGateways()
            routeToTelephony()
            routeToDelivery()
            outcomeTypeRoute()
            routeToUser()
            routeToBadge()
        }

        route("finance") {
            routeToOutcome()
        }

        route("crm") {
            routeToSms()
            routeToMessage()
        }
    }
}