package mimsoft.io.client.order

import ch.qos.logback.classic.db.names.ColumnName
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.order.OrderModel
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.ResponseModel

fun Route.routeToClientOrder() {
    val orderService: OrderRepository = OrderRepositoryImpl
    get("orders") {
        val principal = call.principal<UserPrincipal>()
        val orders = orderService.getClientOrders(clientId = principal?.id, merchantId = principal?.merchantId)
        call.respond(orders.ifEmpty { HttpStatusCode.NoContent })
    }

    get("order/{id}") {
        val principal = call.principal<UserPrincipal>()
        val id = call.parameters["id"]?.toLongOrNull()
        val order = orderService.get(id)
        if (order == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(order)
        return@get
    }

    post("order/model") {
        val principal = call.principal<UserPrincipal>()
        val merchantId = principal?.merchantId
        val order = call.receive<OrderWrapper>()
        val status = orderService.add(order.copy(user = UserDto(id = principal?.id, merchantId = merchantId)))
        call.respond(
            status?.httpStatus?: ResponseModel.SOME_THING_WRONG,
            status?.body?:
            status?.httpStatus?.description?:
            ResponseModel.SOME_THING_WRONG.description)
    }

    post("order") {
        val principal = call.principal<UserPrincipal>()
        val merchantId = principal?.merchantId
        val userId = principal?.id
        val order = call.receive<OrderModel>()
        val status = orderService.addModel(order.copy(user = UserDto(id = userId, merchantId = merchantId)))
        call.respond(
            status.httpStatus,
            status.body?:
            status.httpStatus.description)
    }

    delete("order/{id}") {
        val principal = call.principal<UserPrincipal>()
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(
            status?.httpStatus?: ResponseModel.SOME_THING_WRONG,
            status?.body?: status?.httpStatus?.description?: "Something went wrong")

    }
}