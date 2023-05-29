package mimsoft.io.utils.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import mimsoft.io.admin.routeToAdmin
import mimsoft.io.auth.routeToLogin
import mimsoft.io.entities.address.routeToAddress
import mimsoft.io.entities.app.routeToApp
import mimsoft.io.entities.client.routeToUser
import mimsoft.io.entities.delivery.routeToDelivery
import mimsoft.io.entities.merchant.routeToMerchantProfile
import mimsoft.io.entities.payment.routeToPayment
import mimsoft.io.entities.poster.routeToPoster
import mimsoft.io.entities.sms_gateway.routeToSmsGateways
import mimsoft.io.entities.staff.routeToStaffApis
import mimsoft.io.entities.telephony.routeToTelephony

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("api/v1"){
            routeToAdmin()
            routeToMerchantProfile()
            routeToStaffApis()
            routeToPoster()
            routeToUser()
            routeToLogin()
            routeToAddress()
            routeToApp()
            routeToPayment()
            routeToSmsGateways()
            routeToDelivery()
            routeToTelephony()
        }

        swaggerUI(path = "swagger/users", swaggerFile = "openapi/user.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/staffs", swaggerFile = "openapi/staff.yaml") {
            version = "4.15.5"
        }
        swaggerUI(path = "swagger/orders", swaggerFile = "openapi/orders.yaml") {
            version = "4.15.5"
        }
    }
}

