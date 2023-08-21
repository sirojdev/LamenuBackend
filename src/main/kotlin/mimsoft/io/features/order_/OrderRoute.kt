package mimsoft.io.features.order_

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal


fun Route.routeToOrder() {

    val orderService = OrderService

    route("orders") {

        get("live") {
            val principal = getPrincipal()
            val response = orderService.getAll(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any,
                    "type" to call.parameters["type"] as Any
                )
            )
            call.respond(response.httpStatus, response.body)
        }

        get {
            val principal = getPrincipal()
            val response = orderService.getAll(
                mapOf(
                    "merchantId" to principal?.merchantId as Any,
                    "type" to call.parameters["type"] as Any,
                    "status" to call.parameters["status"] as Any,
                    "search" to call.parameters["search"] as Any,
                    "limit" to (call.parameters["limit"]?.toIntOrNull() ?: 10) as Any,
                    "offset" to (call.parameters["offset"]?.toIntOrNull() ?: 0) as Any
                )
            )
            call.respond(response.httpStatus, response.body)
        }
    }

}

