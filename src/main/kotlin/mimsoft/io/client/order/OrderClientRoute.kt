package mimsoft.io.client.order

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToClientOrder() {
    val orderService = OrderService
    val log: Logger = LoggerFactory.getLogger("routeToClientOrder")
    get("orders") {
        val pr = getPrincipal()
        val map: MutableMap<String, *>
        val status = call.parameters["status"]
        log.info("status {}", status)
        val response = OrderService.getAll(
            mapOf(
                "clientId" to pr?.userId,
                "merchantId" to pr?.merchantId,
                "status" to status,
                "limit" to call.parameters["limit"]?.toLongOrNull(),
                "offset" to call.parameters["offset"]?.toLongOrNull()
            )
        )
        call.respond(response.httpStatus, response.body)
        return@get
    }

    get("order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val order = orderService.get(id)
        call.respond(order.httpStatus, order.body)
    }

    post("order") {
        val principal = call.principal<BasePrincipal>()
        val appKey = call.parameters["appKey"]?.toLongOrNull()
        val userId = principal?.userId
        val order = call.receive<Order>()
        val status = orderService.post(
            order.copy(
                user = UserDto(id = userId),
                merchant = MerchantDto(id = appKey)
            )
        )
        call.respond(status.httpStatus, status.body)
    }

    delete("order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(status.httpStatus, status.body)

    }
}