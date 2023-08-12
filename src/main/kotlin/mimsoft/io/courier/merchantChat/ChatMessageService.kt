package mimsoft.io.courier.merchantChat

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import java.util.*

object ChatMessageService {
    val chatConnections = Collections.synchronizedSet<ChatConnections?>(LinkedHashSet())
    val messageService = ChatMessageRepository
    suspend fun sendMessage(
        to: Long?,
        message: ChatMessageSaveDto
    ) {
        val sender = message.sender
        if (sender == Sender.COURIER) {
            println("list ${ChatMessageService.toString()}")
            val connectionList = chatConnections.filter { it.id == to && it.sender != message.sender }
            println(connectionList.toString())
            var operator: Boolean = false
            for (connection in connectionList) {
                if (connection?.session != null) {
                    operator = true
                    println("inside connection")
                    val rs = connection?.session?.send(message.message.toString())
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
                if (connection?.session != null) {
                    connection?.session?.send(message.message.toString())
                }
            }
                if (courierConnection?.session != null) {
                    courierConnection?.session?.send(message.message.toString())
                    messageService.addMessage(message, to, true)
                }else{
                    messageService.addMessage(message, to, false)
                }
        }


    }
}