package mimsoft.io.routing.v1.client

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderRateModel
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.plugins.getPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min

fun Route.routeToClientOrder() {
    val orderService = OrderService
    val log: Logger = LoggerFactory.getLogger("routeToClientOrder")
    get("orders") {
        val pr = getPrincipal()
        var search = call.parameters["search"]
        if (search == null) {
            search = ""
        }
        val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
        val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
        log.info("search {}", search)
        val response = OrderService.getAll(
            mapOf(
                "clientId" to pr?.userId,
                "merchantId" to pr?.merchantId,
                "search" to search,
                "limit" to limit,
                "offset" to offset
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
        val pr = getPrincipal()
        val userId = pr?.userId
        val merchantId = pr?.merchantId
        val order = call.receive<Order>()
        val status = orderService.post(
            order.copy(
                user = UserDto(id = userId),
                merchant = MerchantDto(id = merchantId)
            )
        )
        call.respond(status.httpStatus, status.body)
    }

    delete("order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(status.httpStatus, status.body)
    }

    put ("order/rate") {
        val pr = getPrincipal()
        val userId = pr?.userId
        val rate = call.receive<OrderRateModel>()
        val response = orderService.orderRate(rate = rate.copy(userId = userId))
        call.respond(response)
    }
}