package mimsoft.io.features.courier.checkout

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCourierTransaction() {
    route("courier/transaction") {
        val courierTransactionService = CourierTransactionService
        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val dto = call.receive<CourierTransactionDto>()
            val result = courierTransactionService.add(dto.copy(merchantId = merchantId, branch = BranchDto(id = branchId)))
            call.respond(CourierTransactionId(result))
            return@post
        }

        get("getByCourier") {
            val merchantId = call.parameters["appKey"]
            val branchId = call.parameters["branchId"]
            val courierId = call.parameters["courierId"]
            val result = courierTransactionService.getByCourierId(
                courierId = courierId?.toLongOrNull(),
                merchantId = merchantId?.toLongOrNull(),
                branchId = branchId?.toLongOrNull()
            )
            if (result.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(result)
            return@get
        }
    }
}

data class CourierTransactionId(
    val id: Long? = null
)