package mimsoft.io.client.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.OrderModel
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToClientOrder() {
    val orderService: OrderRepository = OrderRepositoryImpl
    get("orders") {
        val pr = call.principal<UserPrincipal>()
        val response: Any
        val clientId = pr?.id
        val merchantId = pr?.merchantId
        val filter = call.parameters["status"]
        val limit = call.parameters["limit"]?.toLongOrNull() ?: 10
        val offset = call.parameters["offset"]?.toLongOrNull() ?: 0
        response = OrderRepositoryImpl.getModelListUser(
            clientId = clientId,
            merchantId = merchantId,
            filter = filter,
            limit = limit,
            offset = offset
        )
        call.respond(response)
        return@get
    }

    get("order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val order = OrderRepositoryImpl.getModel(id)
        if (order == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(order)
        return@get
    }

    post("order/model") {
        val principal = call.principal<BasePrincipal>()
        val merchantId = principal?.merchantId
        val order = call.receive<OrderWrapper>()
        val status = orderService.add(order.copy(user = UserDto(id = principal?.userId, merchantId = merchantId)))
        call.respond(
            status?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
            status?.body ?: status?.httpStatus?.description ?: ResponseModel.SOME_THING_WRONG.description
        )
    }

    post("order") {
        val principal = call.principal<BasePrincipal>()
        val merchantId = principal?.merchantId
        val userId = principal?.userId
        val order = call.receive<OrderModel>()
        val status = orderService.addModel(order.copy(user = UserDto(id = userId, merchantId = merchantId)))
        call.respond(status.httpStatus, status.body)
    }

    delete("order/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val status = orderService.delete(id)
        call.respond(
            status?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
            status?.body ?: status?.httpStatus?.description ?: "Something went wrong"
        )

    }
}