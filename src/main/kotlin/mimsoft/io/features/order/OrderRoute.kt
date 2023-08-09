package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToOrder() {

    val repository: OrderRepository = OrderRepositoryImpl

    route("orders") {
        get("live") {
            val type = call.parameters["type"]
            val orders = repository.getLiveOrders(type = type.toString())
            val orderDto = orders?.data
            if (orderDto == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orderDto)
        }
        get("all") {
            val orders = repository.getAll()
            call.respond(orders)
        }
        get("history") {
            val pr = call.principal<MerchantPrincipal>()
            val response: Any
            val merchantId = pr?.merchantId
            val filter = call.parameters["filter"]
            val limit = call.parameters["limit"]?.toLongOrNull()
            val offset = call.parameters["offset"]?.toLongOrNull()
            response = OrderRepositoryImpl.getOrderHistoryMerchant(
                merchantId = merchantId,
                filter = filter,
                limit = limit,
                offset = offset
            )
            call.respond(response)
            return@get
        }

        /*get {
            val pr = call.principal<MerchantPrincipal>()
            val merchantId = pr?.merchantId
            val search = call.parameters["search"]
            val status = call.parameters["status"]
            val type = call.parameters["type"]
            val limit = call.parameters["limit"]?.toIntOrNull()
            val offset = call.parameters["offset"]?.toIntOrNull()
            val orders = repository.getAll(search, merchantId, status, type, limit, offset).data
            if (orders == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, orders)
        }*/


        get("") {
            val pr = call.principal<MerchantPrincipal>()
            val response: Any
            val merchantId = pr?.merchantId
            val filter = call.parameters["filter"]
            val limit = call.parameters["limit"]?.toLongOrNull()
            val offset = call.parameters["offset"]?.toLongOrNull()
            response = OrderRepositoryImpl.getModelListMerchant(
                merchantId = merchantId,
                filter = filter,
                limit = limit,
                offset = offset
            )
            call.respond(response)
            return@get
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val order = repository.get(id)
            if (order != null) {
                call.respond(HttpStatusCode.OK, order)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/create") {
            val order = call.receive<OrderWrapper>()
            val status = repository.add(order)
            call.respond(
                status?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
                status?.body ?: "Something went wrong"
            )
        }

        put {
            val order = call.receive<OrderDto>()
            repository.update(order)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id != null) {
                val deleted = repository.delete(id)
                call.respond(
                    deleted?.httpStatus ?: ResponseModel.SOME_THING_WRONG,
                    deleted?.body ?: "Something went wrong"
                )
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        put("update/details"){
            val detail = call.receive<OrderDetails>()
            if(detail.orderId == null){
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val response = repository.updateDetails(detail = detail)
            call.respond(response)
            return@put
        }
    }
}
