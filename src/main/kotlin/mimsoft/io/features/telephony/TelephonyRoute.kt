package mimsoft.io.features.telephony

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToTelephony(){
    get("telephony"){
        val merchantId = 1L
        val telephony = TelephonyService.get(merchantId = merchantId)
        if(telephony == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(telephony)
    }

    put ("telephony"){
        val telephony = call.receive<TelephonyDto>()
        val merchantId = 1L
        TelephonyService.update(telephony.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

}