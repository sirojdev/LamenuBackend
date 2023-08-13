package mimsoft.io.features.promo

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToPromo() {
    val promoService = PromoService
    route("promo") {
        get {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val promoList = promoService.getAll(merchantId = merchantId)
            mimsoft.io.utils.plugins.LOGGER.info("promoList: ${GSON.toJson(promoList)}")
            if (promoList.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(HttpStatusCode.OK, promoList)
        }

        post {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val promoDto = call.receive<PromoDto>()
            val response = promoService.add(promoDto.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, PromoId(response))
        }

        put {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val promo = call.receive<PromoDto>()
            val updated = promoService.update(promo.copy(merchantId = merchantId))
            if (updated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.InternalServerError)
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
            if (promo != null) {
                call.respond(promo)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }

        delete("{id}") {
            val principal = call.principal<BasePrincipal>()
            val merchantId = principal?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id != null) {
                val deleted = promoService.delete(id = id, merchantId = merchantId)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, deleted)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }
}

data class PromoId(
    val id: Long? = null
)