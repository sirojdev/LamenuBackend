package mimsoft.io.courier

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.courier.location.CourierConnection
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.ChatMessageSaveDto
import mimsoft.io.courier.merchantChat.ChatMessageService
import mimsoft.io.courier.merchantChat.Sender
import mimsoft.io.courier.transaction.CourierConnectDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.operator.socket.AcceptedDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.principal.BasePrincipal
import java.sql.Timestamp

fun Route.toCourierSocket() {
    route("courier") {
        authenticate("courier") {
            webSocket("socket") {
                val principal = call.principal<BasePrincipal>()
                val staffId = principal?.staffId
                val merchantId = principal?.merchantId
                val uuid = principal?.uuid
                try {
                    println(principal?.staffId)
//                    CourierService.updateIsActive(staffId, true)
                    CourierSocketService.setConnection(
                        CourierConnection(
                            staffId = staffId,
                            merchantId = merchantId,
                            uuid = uuid,
                            session = this
                        )
                    )
                    ChatMessageService.sendNotReadMessageInfoCourier(staffId, this)
                    for (frame in incoming) {
                        val conn = CourierSocketService.setConnection(
                            CourierConnection(
                                staffId = staffId,
                                merchantId = merchantId,
                                uuid = uuid,
                                session = this
                            )
                        )
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        println("connection  ${conn.session}")
                        println("text -> $receivedText")
                        val data: SocketData? = Gson().fromJson(receivedText, SocketData::class.java)
                        if (data?.type == SocketType.CHAT) {
                            val chatMessage: ChatMessageDto? =
                                Gson().fromJson(data.data.toString(), ChatMessageDto::class.java)
                            if (chatMessage != null) {
                                val time = Timestamp(System.currentTimeMillis())
                                if (conn.session != null) {
                                    ChatMessageService.sendMessageToOperator(
                                        ChatMessageSaveDto(
                                            fromId = staffId,
                                            toId = merchantId,
                                            sender = Sender.COURIER,
                                            time = time,
                                            message = chatMessage.message
                                        )
                                    )
                                    conn.session!!.send(
                                        Gson().toJson(
                                            SocketData(
                                                type = SocketType.RESPONSE_CHAT,
                                                data = Gson().toJson(chatMessage.copy(time = GSON.toJson(time)))
                                            )
                                        )
                                    )
                                }
                            }
                        } else if (data?.type == SocketType.LOCATION) {
                            val location: CourierLocationHistoryDto? =
                                Gson().fromJson(data.data.toString(), CourierLocationHistoryDto::class.java)
                            println("location -> ${location.toString()}")
                            if (location != null) {
                                CourierLocationHistoryService.add(
                                    location.copy(
                                        merchantId = merchantId,
                                        staffId = staffId,
                                        time = Timestamp(System.currentTimeMillis())
                                    )
                                )
                                /**
                                 * ULANGAN BARCHA OPERATORLARGA COURIER NI LOCATION JUNATISH UCHUN
                                 * */
                                CourierSocketService.sendLocationToOperators(
                                    location.copy(
                                        staffId = staffId,
                                        merchantId = merchantId
                                    )
                                )
                            }
                        } else if (data?.type == SocketType.ACCEPT) {
                            val response = Gson().fromJson(data.data.toString(), AcceptedDto::class.java)
                            if (response != null) {
                                if (response.status == "ACCEPTED") {
                                    val order = OperatorSocketService.acceptedOrder(response, staffId)
                                    if (order) {
                                        conn.session?.send(
                                            Gson().toJson(
                                                SocketData(
                                                    type = SocketType.RESPONSE_ORDER,
                                                    data = Gson().toJson(response.copy(status = "ACCEPTED"))
                                                )
                                            )
                                        )
                                    } else {
                                        val result = SocketData(
                                            type = SocketType.RESPONSE_ORDER,
                                            data = Gson().toJson(response.copy(status = "NOT_ACCEPTED"))
                                        )
                                        conn.session?.send(Gson().toJson(result))
                                    }
                                } else if (response.status == "NOT_ACCEPTED") {
                                    OperatorSocketService.notAccepted(response, staffId)
                                }
                            }
                        } else if (data?.type == SocketType.CONNECT) {
                            val connect = Gson().fromJson(data.data.toString(), CourierConnectDto::class.java)
                            if (connect.status == true) {
                                CourierService.updateIsActive(staffId, true)
                            } else if (connect.status == false) {
                                CourierService.updateIsActive(staffId, false)
                            }
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    CourierService.updateIsActive(staffId, false)
                    CourierSocketService.courierConnections.removeIf { it.session == this }
                    close(CloseReason(CloseReason.Codes.NORMAL, ""))
                }
            }
        }
    }
}