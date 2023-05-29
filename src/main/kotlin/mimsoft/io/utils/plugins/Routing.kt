package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.admin.routeToAdmin
import mimsoft.io.auth.routeToLogin
import mimsoft.io.entities.address.routeToAddress
import mimsoft.io.entities.client.routeToUser
import mimsoft.io.entities.merchant.routeToMerchantProfile
import mimsoft.io.entities.staff.routeToStaffApis

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("api/v1"){
            routeToAdmin()
            routeToMerchantProfile()
            routeToStaffApis()


            routeToUser()

            routeToLogin()
            routeToAddress()

        }

        swaggerUI(path = "swagger/users", swaggerFile = "openapi/user.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/staffs", swaggerFile = "openapi/staff.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/orders", swaggerFile = "openapi/entities/orders.yaml") {
            version = "4.15.5"
        }
    }
}

