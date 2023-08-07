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
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.merchantChatRoute() {
    route("chat") {
        authenticate("merchant") {
            get("") {
                val principal = call.receive<MerchantPrincipal>()
                val merchantId = principal.merchantId
                val userList = ChatMessageRepository.getAllCourierChat(merchantId);
                if (userList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,userList)
            }
        }
        authenticate("staff") {
            get("/merchant") {
                val principal = call.receive<StaffPrincipal>()
                val merchantId = principal.merchantId
                val merchant = MerchantRepositoryImp.getMerchantById(merchantId);
                if (merchant==null){
                    call.respond(HttpStatusCode.NotFound)
                }else{
                    call.respond(HttpStatusCode.OK,merchant)
                }
            }
        }
    }
}