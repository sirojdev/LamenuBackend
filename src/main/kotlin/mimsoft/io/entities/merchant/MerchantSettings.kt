package mimsoft.io.entities.merchant

import io.ktor.server.routing.*
import mimsoft.io.entities.branch.routeToBranch
import mimsoft.io.entities.category.routeToCategory
import mimsoft.io.entities.extra.routeToExtra
import mimsoft.io.entities.flat.routeToFlat
import mimsoft.io.entities.label.routeToLabel
import mimsoft.io.entities.menu.routeToMenu
import mimsoft.io.entities.option.routeToOption
import mimsoft.io.entities.order.routeToOrder
import mimsoft.io.entities.product.routeToProduct
import mimsoft.io.entities.room.routeToRoom
import mimsoft.io.entities.staff.routeToStaff
import mimsoft.io.table.routeToTable
import mimsoft.io.telegram_bot.routeToBot


fun Route.routeToMerchantSettings() {

    route("settings") {
        routeToStaff()
        routeToBranch()
        routeToCategory()
        routeToExtra()
        routeToLabel()
        routeToMenu()
        routeToOption()
        routeToProduct()
        routeToOrder()
        routeToTable()
        routeToRoom()
        routeToFlat()
        routeToBot()

    }
}