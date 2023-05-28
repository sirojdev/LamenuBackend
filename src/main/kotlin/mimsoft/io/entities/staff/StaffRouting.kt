package mimsoft.io.entities.staff

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.auth.routeToLogin
import mimsoft.io.entities.branch.routeToBranch
import mimsoft.io.entities.category.routeToCategory
import mimsoft.io.entities.client.routeToUser
import mimsoft.io.entities.extra.routeToExtra
import mimsoft.io.entities.label.routeToLabel
import mimsoft.io.entities.menu.routeToMenu
import mimsoft.io.entities.option.routeToOption
import mimsoft.io.entities.order.routeToOrder
import mimsoft.io.entities.product.routeToProduct
import mimsoft.io.entities.restaurant.routeToRestaurant

fun Route.routeToStaffApis() {
    route("staff") {
        routeToStaffAuth()

        authenticate("access") {
            routeToUser()
            routeToBranch()
            routeToCategory()
            routeToExtra()
            routeToLabel()
            routeToMenu()
            routeToOption()
            routeToRestaurant()
            routeToProduct()
            routeToOrder()
            routeToLogin()
            routeToStaff()
        }
    }
}