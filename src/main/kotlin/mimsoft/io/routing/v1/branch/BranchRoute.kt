package mimsoft.io.routing.v1.branch

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.branchOperator.routeToBranchAdminAuth
import mimsoft.io.features.area.routeToArea
import mimsoft.io.features.cashback.routeToCashback
import mimsoft.io.features.category.routeToCategory
import mimsoft.io.features.courier.checkout.routeToCourierTransaction
import mimsoft.io.features.courier.routeToCourier
import mimsoft.io.features.extra.routeToExtra
import mimsoft.io.features.flat.routeToFlat
import mimsoft.io.features.income.routeToIncome
import mimsoft.io.features.kitchen.routeToKitchen
import mimsoft.io.features.label.routeToLabel
import mimsoft.io.features.merchant_booking.routeToBranchBook
import mimsoft.io.features.option.routeToOption
import mimsoft.io.features.order.routeToOrder
import mimsoft.io.features.outcome.routeToOutcome
import mimsoft.io.features.pantry.routeToPantry
import mimsoft.io.features.product.routeToProduct
import mimsoft.io.features.room.routeToRoom
import mimsoft.io.features.staff.routeToCollector
import mimsoft.io.features.staff.routeToStaff
import mimsoft.io.features.story.routeToStory
import mimsoft.io.features.story_info.routeToStoryInfo
import mimsoft.io.features.table.routeToTable
import mimsoft.io.features.visit.routeToVisits
import mimsoft.io.features.waiters.table.routToWaiters
import mimsoft.io.waiter.routeToWaiter

fun Route.routeToBranchAdmin(){
    route("branch"){
        routeToBranchAdminAuth()

        authenticate("branch") {
            routeToVisits()
            routeToPantry()
            routeToKitchen()
            routeToBranchBook()
            routeToStory()
            routeToStoryInfo()
            routeToCourierTransaction()
            routToWaiters()
            routeToTable()

            route("settings") {
                routeToArea()
                routeToRoom()
                routeToFlat()
                routeToStaff()
                routeToExtra()
                routeToLabel()
                routeToOrder()
                routeToTable()
                routeToOption()
                routeToCourier()
                routeToCollector()
                routeToProduct()
                routeToCashback()
                routeToCategory()
            }

            route("finance") {
                routeToOutcome()
                routeToIncome()
            }
        }
    }
}