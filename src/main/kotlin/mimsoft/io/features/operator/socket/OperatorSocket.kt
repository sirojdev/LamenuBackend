package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.ChatMessageSaveDto
import mimsoft.io.courier.merchantChat.ChatMessageService
import mimsoft.io.courier.merchantChat.Sender
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType

import mimsoft.io.utils.principal.BasePrincipal
import java.sql.Timestamp

fun Route.toOperatorSocket() {
    route("operator") {

        authenticate("operator") {
            /**
             *  OPERATOR UCHUN SOCKET
             * */
            webSocket("socket") {
                try {
                    val principal = call.principal<BasePrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    val uuid = principal?.uuid
                    OperatorSocketService.setConnection(staffId, merchantId, uuid, this)

                    ChatMessageService.sendNotReadMessageInfoOperator( merchantId, this)

                    for (frame in incoming) {
                        val conn = OperatorSocketService.setConnection(staffId, merchantId, uuid, this)
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val data: SocketData? = Gson().fromJson(receivedText, SocketData::class.java)
                        if (data?.type == SocketType.CHAT) {
                            val chatMessage: ChatMessageDto? = Gson().fromJson(data.data.toString(), ChatMessageDto::class.java)
                            if (chatMessage != null) {
                                if (conn.session != null) {
                                    ChatMessageService.sendMessageToCourier(
                                        ChatMessageSaveDto(
                                            fromId = merchantId,
                                            toId = chatMessage.toId,
                                            operatorId = staffId,
                                            sender = Sender.MERCHANT,
                                            time = Timestamp(System.currentTimeMillis()),
                                            message = chatMessage.message
                                        )
                                    )
                                }
                            }
                        }
                    }
                    try {
                        while (isActive) {
                            // Just keep the WebSocket open
                        }
                    } finally {
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Connection closed"))
                    CourierSocketService.locationConnection.removeIf { it.session == this }
                }
            }
        }

    }
}
