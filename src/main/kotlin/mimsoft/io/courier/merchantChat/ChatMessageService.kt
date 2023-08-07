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
        message: ChatMessageDto
    ) {
        val connection = chatConnections.find { it.id == to && it.type != message.type }
        if (connection?.session != null && connection.session!!.isActive) {
            val rs = connection?.session?.send(message.message.toString())
            messageService.addMessage(message, to, true)
        }else{
            messageService.addMessage(message, to, false)
        }
    }
}