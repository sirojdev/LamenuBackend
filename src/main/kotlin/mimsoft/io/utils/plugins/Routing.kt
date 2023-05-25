package mimsoft.io.utils.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
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
import mimsoft.io.staff.routeToStaff
import mimsoft.io.staff.routeToStaffApis

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("api/v1"){
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

            routeToStaffApis()
        }

        swaggerUI(path = "swagger/users", swaggerFile = "openapi/user.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/staffs", swaggerFile = "openapi/staff.yaml") {
            version = "4.15.5"
        }
    }
}

