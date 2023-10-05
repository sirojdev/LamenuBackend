package mimsoft.io.features.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.merchant.repository.MerchantAuthImp
import mimsoft.io.features.merchant.repository.MerchantAuthService
import mimsoft.io.utils.principal.BasePrincipal

fun Route.merchantAuthRoute() {
    val authService: MerchantAuthService = MerchantAuthImp

    post("auth") {
        val merchant = call.receive<MerchantDto>()
        val merchantAuth = authService.auth(merchantDto = merchant)
        if (merchantAuth != null) {
            call.respond(merchantAuth)
        } else
            call.respond(HttpStatusCode.BadRequest)
    }


    authenticate("merchant") {
        post("logout") {
            val merchant = call.principal<BasePrincipal>()
            authService.logout(merchant?.uuid)
            call.respond(HttpStatusCode.OK)
        }
    }
}

