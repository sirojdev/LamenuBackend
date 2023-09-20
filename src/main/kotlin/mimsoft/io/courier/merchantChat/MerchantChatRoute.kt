package mimsoft.io.courier.merchantChat

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.merchantChatRoute() {
    route("chat") {
        authenticate("courier") {
            get("/merchant") {
                val principal = call.principal<BasePrincipal>()
                val merchantId = principal?.merchantId
                val merchant = MerchantRepositoryImp.getMerchantById(merchantId);
                if (merchant==null){
                    call.respond(HttpStatusCode.NotFound)
                }else{
                    call.respond(HttpStatusCode.OK,merchant)
                }
            }
            get("/messages") {
                val principal = call.principal<BasePrincipal>()
                val merchantId = principal?.merchantId
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val courierId = principal?.staffId
                val messageList = ChatMessageRepository.getUserMessages(courierId, merchantId, limit, offset);
                if (messageList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,messageList)
            }
        }
    }
}