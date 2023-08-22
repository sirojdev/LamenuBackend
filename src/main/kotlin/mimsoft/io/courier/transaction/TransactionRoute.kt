package mimsoft.io.courier.transaction

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.courier.checkout.CourierTransactionDto
import mimsoft.io.features.courier.checkout.CourierTransactionId
import mimsoft.io.features.courier.checkout.CourierTransactionService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.principal.BasePrincipal
import java.sql.Timestamp

fun Route.routeToCourierTransaction() {
    val transactionService = TransactionService
    val courierTransactionService = CourierTransactionService
    route("transaction") {
        get {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val courierId = pr?.staffId
            val dto = call.receive<CourierTransactionDto>()
            if (dto.branch == null) {
                val order = OrderService.get(id = dto.order?.id).body as Order
                if (order.id != dto.order?.id || order.status != OrderStatus.DELIVERED.name || order.courier?.id != courierId) {
                    call.respond(HttpStatusCode.MethodNotAllowed)
                }
            }

            val result =
                courierTransactionService.add(
                    dto.copy(
                        merchantId = merchantId,
                        courier = CourierDto(id = courierId),
                        time = Timestamp(System.currentTimeMillis())
                    )
                )
            call.respond(CourierTransactionId(result))
        }

        get("all") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val courierId = pr?.staffId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val result = transactionService.getList(
                courierId = courierId,
                merchantId = merchantId,
                limit = limit,
                offset = offset
            )
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(result)
            return@get
        }

//        get("{id}") {
//            val pr = call.principal<BasePrincipal>()
//            val transactionId = call.parameters["id"]?.toLongOrNull()
//            val merchantId = pr?.merchantId
//            val courierId = pr?.staffId
//            val result = transactionService.getById(courierId = courierId, merchantId = merchantId, transactionId)
//            if (result == null) {
//                call.respond(HttpStatusCode.NotFound)
//                return@get
//            }
//            call.respond(result)
//            return@get
//        }
    }

}