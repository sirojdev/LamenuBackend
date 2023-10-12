package mimsoft.io.features.cashback

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToCashback() {
    val cashbackService = CashbackService
    route("cashback") {
        get("") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val cashbackList = cashbackService.getAll(merchantId = merchantId, branchId = branchId)
            if (cashbackList.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, cashbackList)
        }

        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val cashbackDto = call.receive<CashbackDto>()
            val response = cashbackService.add(cashbackDto.copy(merchantId = merchantId, branchId = branchId))
            call.respond(HttpStatusCode.OK, CashbackId(response))
        }

        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val cashback = call.receive<CashbackDto>()
            val updated = cashbackService.update((cashback.copy(merchantId = merchantId, branchId = branchId)))
            if (updated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val product = cashbackService.get(merchantId = merchantId, id = id, branchId = branchId)
            if (product != null) {
                call.respond(product)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }

        delete("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            } else{
                val response = cashbackService.delete(id = id, merchantId = merchantId, branchId = branchId)
                call.respond(response)
            }
        }
    }
}

data class CashbackId(
    val id: Long? = null
)