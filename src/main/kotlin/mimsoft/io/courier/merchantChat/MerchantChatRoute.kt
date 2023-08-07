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
                val principal = call.principal<MerchantPrincipal>()
                val merchantId = principal?.merchantId
                val userList = ChatMessageRepository.getAllCourierChat(merchantId);
                if (userList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,userList)
            }
            get("messages") {
                val courierId = call.parameters["courierId"]?.toLongOrNull()
                val principal = call.principal<MerchantPrincipal>()
                val merchantId = principal?.merchantId
                val messageList = ChatMessageRepository.getUserMessages(merchantId,courierId);
                if (messageList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,messageList)
            }
        }
        authenticate("staff") {
            get("/merchant") {
                val principal = call.principal<StaffPrincipal>()
                val merchantId = principal?.merchantId
                val merchant = MerchantRepositoryImp.getMerchantById(merchantId);
                if (merchant==null){
                    call.respond(HttpStatusCode.NotFound)
                }else{
                    call.respond(HttpStatusCode.OK,merchant)
                }
            }
            get("/merchant/messages") {
                val principal = call.principal<StaffPrincipal>()
                val merchantId = principal?.merchantId
                val courierId = principal?.staffId
                val messageList = ChatMessageRepository.getUserMessages(courierId,merchantId);
                if (messageList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,messageList)
            }
        }
    }
}