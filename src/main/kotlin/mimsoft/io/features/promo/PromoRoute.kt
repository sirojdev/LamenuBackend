package mimsoft.io.features.promo

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToPromo() {

    val promoService = PromoService

    route("promo") {

        get {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
            val promoList = promoService.getAll(merchantId = merchantId, limit, offset)
            mimsoft.io.utils.plugins.LOGGER.info("promoList: ${GSON.toJson(promoList)}")
            if (promoList.data?.isEmpty() == true) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(promoList)
        }

        post {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val promoDto = call.receive<PromoDto>()
            val productDiscount = promoDto.productDiscount?.times(100)
            val deliveryDiscount = promoDto.deliveryDiscount?.times(100)
            val minAmount = promoDto.minAmount?.times(100)
            val amount = promoDto.amount?.times(100)
            val response = promoService.add(
                promoDto.copy(
                    merchantId = merchantId,
                    productDiscount = productDiscount,
                    deliveryDiscount = deliveryDiscount,
                    minAmount = minAmount,
                    amount = amount
                )
            )
            call.respond(PromoId(response))
        }

        put {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val promo = call.receive<PromoDto>()
            val updated = promoService.update(promo.copy(merchantId = merchantId))
            if (updated) call.respond(updated)
            call.respond(HttpStatusCode.NoContent)
        }

        delete("{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            val deleted = promoService.delete(id = id, merchantId = merchantId)
            if (deleted) call.respond(deleted)
            call.respond(HttpStatusCode.NoContent)
        }

        get("{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val promo = promoService.get(merchantId = merchantId, id = id)
            call.respond(promo ?: HttpStatusCode.NoContent)
        }
    }
}

data class PromoId(
    val id: Long? = null
)