package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.announcement.routeToAnnounce
import mimsoft.io.features.app.routeToApp
import mimsoft.io.features.badge.routeToBadge
import mimsoft.io.features.branch.routeToBranch
import mimsoft.io.features.cashback.routeToCashback
import mimsoft.io.features.category.routeToCategory
import mimsoft.io.features.category_group.routeToCategoryGroup
import mimsoft.io.features.client_promo.routeToClientPromo
import mimsoft.io.features.courier.checkout.routeToCourierTransaction
import mimsoft.io.features.courier.routeToCourier
import mimsoft.io.features.delivery.routeToDelivery
import mimsoft.io.features.extra.routeToExtra
import mimsoft.io.features.flat.routeToFlat
import mimsoft.io.features.kitchen.routeToKitchen
import mimsoft.io.features.label.routeToLabel
import mimsoft.io.features.merchant.merchantAuthRoute
import mimsoft.io.features.merchant.order.routeToMerchantOrder
import mimsoft.io.routing.merchant.routeToUserUser
import mimsoft.io.features.merchant_booking.routeToMerchantBook
import mimsoft.io.features.message.routeToMessage
import mimsoft.io.features.notification.routeToNotification
import mimsoft.io.features.option.routeToOption
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.outcome.routeToOutcome
import mimsoft.io.features.outcome_type.routeToOutcomeType
import mimsoft.io.features.pantry.routeToPantry
import mimsoft.io.features.payment.routeToPayment
import mimsoft.io.features.poster.routeToPoster
import mimsoft.io.features.product.product_extra.routeToProductExtra
import mimsoft.io.features.product.product_label.routeToProductLabel
import mimsoft.io.features.product.product_option.routeToProductOption
import mimsoft.io.features.product.routeToProduct
import mimsoft.io.features.promo.routeToPromo
import mimsoft.io.features.room.routeToRoom
import mimsoft.io.features.sms.routeToSms
import mimsoft.io.features.sms_gateway.routeToSmsGateways
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.features.staff.routeToStaff
import mimsoft.io.features.staff.routeToStaffProfile
import mimsoft.io.features.story.routeToStory
import mimsoft.io.features.story_info.routeToStoryInfo
import mimsoft.io.features.table.routeToTable
import mimsoft.io.features.telegram_bot.routeToBot
import mimsoft.io.features.telephony.routeToTelephony
import mimsoft.io.features.visit.routeToVisits
import mimsoft.io.routing.merchant.routeToMerchantInfo
import mimsoft.io.routing.merchant.routeToMerchantProfile
import mimsoft.io.routing.merchant.routeToOrderByCourierAndCollector

fun Route.routeToMerchant() {

    route("merchant") {
        merchantAuthRoute()
        routeToMerchantInfo()

        authenticate("merchant") {

            routeToVisits()
            routeToPantry()
            routeToKitchen()
            routeToMerchantBook()
            routeToMerchantOrder()
            routeToMerchantProfile()
            routeToOrderByCourierAndCollector()
            routeToStory()
            routeToStoryInfo()
            routeToCategoryGroup()
            routeToAnnounce()
            routeToCourierTransaction()
            routeToClientPromo()


            route("settings") {
                routeToApp()
                routeToBot()
                routeToRoom()
                routeToFlat()
                routeToBadge()
                routeToStaff()
                routeToExtra()
                routeToLabel()
                routeToOrder()
                routeToTable()
                routeToOption()
                routeToBranch()
                routeToPoster()
                routeToCourier()
                routeToCollector()
                routeToPayment()
                routeToProduct()
                routeToCashback()
                routeToDelivery()
                routeToCategory()
                routeToTelephony()
                routeToOutcomeType()
                routeToSmsGateways()
                routeToProductLabel()
                routeToProductExtra()
                routeToProductOption()
            }

            route("finance") {
                routeToOutcome()
            }
            route("crm") {
                routeToSms()
                routeToMessage()
                routeToPromo()
                routeToUserUser()
                routeToNotification()
            }
        }
    }
}