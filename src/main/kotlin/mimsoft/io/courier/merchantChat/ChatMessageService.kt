package mimsoft.io.courier.merchantChat

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import java.sql.Timestamp
import java.util.*

object ChatMessageService {
    val chatConnections: MutableSet<ChatConnections> = Collections.synchronizedSet(LinkedHashSet())
    private val messageService = ChatMessageRepository
    suspend fun sendMessage(
        to: Long?,
        message: ChatMessageSaveDto
    ) {
        val sender = message.sender
        if (sender == Sender.COURIER) {
            println("list $ChatMessageService")
            val connectionList = chatConnections.filter { it.id == to && it.sender != message.sender }
            println(connectionList.toString())
            var operator = false
            for (connection in connectionList) {
                if (connection.session != null) {
                    operator = true
                    println("inside connection")
                    connection.session?.send(message.message.toString())
                    messageService.addMessage(message, to, true)
                }
            }
            if (!operator) {
                messageService.addMessage(message, to, false)
            }
        } else {
            val courierConnection =
                chatConnections.find { it.id == to && it.sender != message.sender }
            val otherOperators =
                chatConnections.filter { it.id == message.fromId && it.sender == message.sender && it.operatorId != message.operatorId }
            for (connection in otherOperators) {
                if (connection.session != null) {
                    connection.session?.send(message.message.toString())
                }
            }
            if (courierConnection?.session != null) {
                courierConnection.session?.send(message.message.toString())
                messageService.addMessage(message, to, true)
            } else {
                messageService.addMessage(message, to, false)
            }
        }
    }

    suspend fun sendNotReadMessageInfo(
        sender: Sender?,
        fromId: Long?,
        connection: ChatConnections?,
        operatorId: Long?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        if (connection == null) {
            val getter = if (sender == Sender.MERCHANT) {
                Sender.COURIER
            } else {
                Sender.MERCHANT
            }
            val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(fromId, getter)
            if (notReadMsgInfo.isNotEmpty()) {
                defaultWebSocketServerSession.send(Gson().toJson(notReadMsgInfo))
                // all message status to send
                ChatMessageRepository.readMessages(fromId, getter)
            }
            chatConnections += ChatConnections(
                id = fromId,
                sender = sender,
                operatorId = operatorId,
                connectAt = Timestamp(System.currentTimeMillis()),
                session = defaultWebSocketServerSession
            )
        }

    }
}