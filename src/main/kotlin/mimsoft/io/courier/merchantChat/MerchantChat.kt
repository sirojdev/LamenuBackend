package mimsoft.io.courier.merchantChat

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.AdminConnection
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.location.Connection
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService

fun Route.routeToMerchantChat() {
    route("chat")    {

//        webSocket("courier")
//            val courierId = call.parameters["courierId"]?.toLongOrNull()
//            val merchantId = call.parameters["merchantId"]?.toLongOrNull()
//            try {
//                for (frame in incoming) {
//                    frame as? Frame.Text ?: continue
//                    val receivedText = frame.readText()
//                    CourierSocketService.chatConnections.removeIf { it.staffId == courierId }
//                    CourierSocketService.chatConnect(
//                        ChatConnections(
//                            session = this,
//                            merchantId = merchantId,
//                            staffId = courierId
//                        )
//                    )
//                }
//                try {
//                    while (isActive) {
//                        // Just keep the WebSocket open
//                    }
//                } finally {
//                    println("Admin disconnected")
//                }
//            } catch (e: Exception) {
//                println(e.localizedMessage)
//            } finally {
//                close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
//                CourierSocketService.connections.removeIf { it.session == this }
//            }
//
//        }
    }
}