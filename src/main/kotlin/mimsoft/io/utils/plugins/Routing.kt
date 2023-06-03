package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.routing.routeToV1

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1"){
            routeToV1()
        }
        swaggerUI(path = "docs/merchant", swaggerFile = "openapi/merchant/merchant.yaml") {
            version = "4.15.5"
        }

        swaggerUI(path = "swagger/user", swaggerFile = "openapi/user.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/staff", swaggerFile = "openapi/staff/staffDoc.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/staff/order", swaggerFile = "openapi/staff/order.yaml") {
            version = "4.15.5"
        }

        swaggerUI(path = "swagger/admin", swaggerFile = "openapi/admin/merchant.yaml") {
            version = "4.15.5"
        }

    }
}

