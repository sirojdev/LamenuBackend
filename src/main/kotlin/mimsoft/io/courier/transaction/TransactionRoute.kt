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
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.OrderStatus
import java.sql.Timestamp

fun Route.routeToCourierTransaction() {
    val transactionService = TransactionService
    val courierTransactionService = CourierTransactionService
    route("transaction") {
        post {
            val pr = call.principal<StaffPrincipal>()
            val merchantId = pr?.merchantId
            val courierId = pr?.staffId
            val dto = call.receive<CourierTransactionDto>()
            if (dto.branch == null) {
                val orderDto = OrderRepositoryImpl.get(id = dto.order?.order?.id, merchantId = merchantId)
                if (orderDto.order?.id != dto.order?.order?.id || orderDto.order?.status != OrderStatus.DELIVERED.name || orderDto.order.courier?.id != courierId) {
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
            return@post
        }

        get("") {
            val pr = call.principal<StaffPrincipal>()
            val merchantId = pr?.merchantId
            val courierId = pr?.staffId
            val result = transactionService.getList(courierId = courierId, merchantId = merchantId)
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(result)
            return@get
        }

        get("{id}") {
            val pr = call.principal<StaffPrincipal>()
            val transactionId = call.parameters["id"]?.toLongOrNull()
            val merchantId = pr?.merchantId
            val courierId = pr?.staffId
            val result = transactionService.getById(courierId = courierId, merchantId = merchantId, transactionId)
            if (result == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(result)
            return@get
        }
    }

}