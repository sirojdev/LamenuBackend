package mimsoft.io.waiter.socket

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.ChatMessageSaveDto
import mimsoft.io.courier.merchantChat.ChatMessageService
import mimsoft.io.courier.merchantChat.Sender
import mimsoft.io.courier.transaction.CourierConnectDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.waiter.WaiterService
import java.sql.Timestamp


fun Route.toWaiterSocket() {
    route("waiter") {
        authenticate("waiter") {
            /**
             *  WAITER UCHUN SOCKET
             * */
            webSocket("socket") {
                val principal = call.principal<BasePrincipal>()
                val staffId = principal?.staffId
                val merchantId = principal?.merchantId
                val uuid = principal?.uuid
                try {
                    WaiterSocketService.setConnection(staffId, merchantId, uuid, this)
                    for (frame in incoming) {
                        val conn = WaiterSocketService.setConnection(staffId, merchantId, uuid, this)
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val data: SocketData? = Gson().fromJson(receivedText, SocketData::class.java)
                        when (data?.type) {
                            SocketType.CONNECT -> connectMethod(data, staffId)
                            SocketType.NEW_ORDER -> newOrder(data, conn, staffId, merchantId)
                            else -> {}
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
                    WaiterSocketService.waiterSocketService.removeIf { it.session == this }
                    WaiterService.updateIsActive(staffId, false)
                }
            }
        }
    }
}

suspend fun newOrder(data: SocketData, conn: WaiterSocketConnection, staffId: Long?, merchantId: Long?) {
    val newOrder = Gson().fromJson(data.data.toString(), WaiterNewOrderDto::class.java)
    if (newOrder.roomId == null || newOrder.tableId == null) {

    } else {
//        WaiterService.jo
    }
}

suspend fun connectMethod(data: SocketData, staffId: Long?) {
    val connect = Gson().fromJson(data.data.toString(), WaiterConnectDto::class.java)
    if (connect.status == true) {
        WaiterService.updateIsActive(staffId, true)
    } else if (connect.status == false) {
        WaiterService.updateIsActive(staffId, false)
    }
}
