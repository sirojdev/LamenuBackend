package mimsoft.io.features.operator.chat

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToOperatorChat(){
    route("chat"){
        authenticate("courier") {
            get("") {
                val principal = call.principal<BasePrincipal>()
                val merchantId = principal?.merchantId
                val userList = ChatMessageRepository.getAllCourierChat(merchantId)
                if (userList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,userList)
            }
            get("messages") {
                val courierId = call.parameters["courierId"]?.toLongOrNull()
                val principal = call.principal<BasePrincipal>()
                val merchantId = principal?.merchantId
                val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
                val messageList = ChatMessageRepository.getUserMessages(merchantId,courierId,limit,offset);
                if (messageList.isEmpty()){
                    call.respond(HttpStatusCode.NoContent)
                }
                call.respond(HttpStatusCode.OK,messageList)
            }
        }
    }
}