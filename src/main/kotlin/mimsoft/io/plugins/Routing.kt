package mimsoft.io.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import mimsoft.io.entities.branch.routeToBranch
import mimsoft.io.entities.category.routeToCategory
import mimsoft.io.entities.client.routeToUser
import mimsoft.io.entities.extra.routeToExtra
import mimsoft.io.entities.label.routeToLabel
import mimsoft.io.entities.menu.routeToMenu
import mimsoft.io.entities.option.routeToOption
import mimsoft.io.entities.product.routeToProduct
import mimsoft.io.entities.restaurant.routeToRestaurant

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
        }

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
    }
}
