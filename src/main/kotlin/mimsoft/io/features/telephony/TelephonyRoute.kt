package mimsoft.io.features.telephony

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToTelephony(){
    get("telephony"){
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val telephony = TelephonyService.get(merchantId = merchantId)
        if(telephony == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(telephony)
    }

    put ("telephony"){
        val telephony = call.receive<TelephonyDto>()
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        TelephonyService.add(telephony.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

}