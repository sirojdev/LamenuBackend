package mimsoft.io.courier.merchantChat

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import mimsoft.io.courier.CourierSocketService
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import mimsoft.io.utils.plugins.GSON

object ChatMessageService {
  private val messageService = ChatMessageRepository

  suspend fun sendMessageToOperator(message: ChatMessageSaveDto): Long? {
    val connectionList =
      OperatorSocketService.operatorConnections.filter { it.merchantId == message.toId }
    var operator = false
    for (connection in connectionList) {
      if (connection.session != null) {
        operator = true
        println("inside connection")
        val dto = SocketData(type = SocketType.CHAT, data = GSON.toJson(message))
        connection.session?.send(GSON.toJson(dto))
        return messageService.addMessage(message, message.toId, true)
      }
    }
    if (!operator) {
      return messageService.addMessage(message, message.toId, false)
    }
    return null
  }

  suspend fun sendMessageToCourier(message: ChatMessageSaveDto) {
    val courierConnection =
      CourierSocketService.courierConnections.find {
        it.staffId == message.toId && it.merchantId == message.fromId
      }
    val otherOperators =
      OperatorSocketService.operatorConnections.filter {
        it.merchantId == message.fromId && it.staffId != message.operatorId
      }
    for (connection in otherOperators) {
      if (connection.session != null) {
        GSON.toJson(message)
        val dto = SocketData(type = SocketType.CHAT, data = GSON.toJson(message))
        connection.session?.send(GSON.toJson(dto))
      }
    }
    if (courierConnection?.session != null) {
      val dto = SocketData(type = SocketType.CHAT, data = GSON.toJson(message))
      courierConnection.session?.send(GSON.toJson(dto))
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
      val dto = SocketData(type = SocketType.NOT_READ, data = GSON.toJson(notReadMsgInfo))
      session.send(GSON.toJson(dto))
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
      val dto = SocketData(type = SocketType.NOT_READ, data = GSON.toJson(notReadMsgInfo))
      session.send(GSON.toJson(dto))
    }
    // all message status to send
    ChatMessageRepository.readMessages(merchantId, Sender.COURIER)
  }
}
