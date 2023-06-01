package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.admin.routeToAdmin
import mimsoft.io.auth.routeToLogin
import mimsoft.io.entities.address.routeToAddress
import mimsoft.io.entities.client.routeToUser
import mimsoft.io.entities.menu.routeToClient
import mimsoft.io.entities.merchant.routeToMerchantProfile
import mimsoft.io.entities.seles.routeToSales
import mimsoft.io.entities.staff.routeToStaffApis

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1"){
            routeToAdmin()
            routeToMerchantProfile()
            routeToClient()
            routeToStaffApis()
            routeToUser()
            routeToLogin()
            routeToAddress()

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
        swaggerUI(path = "swagger/merchant/staff", swaggerFile = "openapi/merchant/staff.yaml") {
            version = "4.15.5"
        }
    }
}

