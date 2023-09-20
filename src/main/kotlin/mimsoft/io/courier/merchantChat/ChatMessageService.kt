package mimsoft.io.courier.merchantChat

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.courier.CourierSocketService
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import java.sql.Timestamp
import java.util.*

object ChatMessageService {
    private val messageService = ChatMessageRepository
    suspend fun sendMessageToOperator(
        message: ChatMessageSaveDto
    ) {
        val connectionList = OperatorSocketService.operatorConnections.filter { it.merchantId == message.toId }
        var operator = false
        for (connection in connectionList) {
            if (connection.session != null) {
                operator = true
                println("inside connection")
                val dto = SocketData(type = SocketType.CHAT, data = Gson().toJson(message))
                connection.session?.send(Gson().toJson(dto))
                messageService.addMessage(message, message.toId, true)
            }
        }
        if (!operator) {
            messageService.addMessage(message, message.toId, false)
        }

    }
    suspend fun sendMessageToCourier(
        message: ChatMessageSaveDto
    ) {
        val courierConnection= CourierSocketService.courierConnections.find { it.staffId == message.toId && it.merchantId==message.fromId }

            val otherOperators =
                OperatorSocketService.operatorConnections.filter { it.merchantId == message.fromId &&  it.staffId != message.operatorId }
            for (connection in otherOperators) {
                if (connection.session != null) {
                    val dto = SocketData(type = SocketType.CHAT, data =Gson().toJson(message) )
                    connection.session?.send(Gson().toJson(dto))
                }
            }
            if (courierConnection?.session != null) {
                val dto = SocketData(type = SocketType.CHAT, data =Gson().toJson(message) )
                courierConnection.session?.send(Gson().toJson(dto))
                messageService.addMessage(message, message.toId, true)
            } else {
                messageService.addMessage(message, message.toId, false)
            }

    }

    suspend fun sendNotReadMessageInfoCourier(
        staffId: Long?,
        session: DefaultWebSocketServerSession,
    ) {
        val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(staffId, Sender.MERCHANT)
        if (notReadMsgInfo.isNotEmpty()) {
            val dto = SocketData(type = SocketType.NOT_READ, data =Gson().toJson(notReadMsgInfo))
            session.send(Gson().toJson(dto))
        }
        // all message status to send
        ChatMessageRepository.readMessages(staffId, Sender.MERCHANT)
    }
    suspend fun sendNotReadMessageInfoOperator(
        merchantId: Long?,
        session: DefaultWebSocketServerSession,
    ) {
        val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(merchantId, Sender.COURIER)
        if (notReadMsgInfo.isNotEmpty()) {
            val dto = SocketData(type = SocketType.NOT_READ, data =Gson().toJson(notReadMsgInfo) )
            session.send(Gson().toJson(dto))
        }
        // all message status to send
        ChatMessageRepository.readMessages(merchantId, Sender.COURIER)
    }
}