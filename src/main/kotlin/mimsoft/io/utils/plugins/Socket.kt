package mimsoft.io.utils.plugins

import com.google.gson.Gson
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import mimsoft.io.courier.location.AdminConnection
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.location.Connection
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.courier.merchantChat.*
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.services.socket.MessageModel
import mimsoft.io.services.socket.SocketEntity
import mimsoft.io.services.socket.SocketService
import mimsoft.io.services.socket.StatusConnection
import mimsoft.io.utils.principal.MerchantPrincipal
import java.sql.Timestamp
import java.time.Duration

fun Application.configureSocket() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(60)
        timeout = Duration.ofSeconds(60)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        authenticate("staff") {
            webSocket("location") {
                try {
                    val principal = call.principal<StaffPrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    val connection = CourierSocketService.connections.find { it.staffId == staffId }
                    if (connection == null) {
                        CourierSocketService.connections += Connection(
                            staffId = staffId,
                            session = this,
                            merchantId = principal?.merchantId,
                            connectAt = Timestamp(System.currentTimeMillis())
                        )
                    }
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val location: CourierLocationHistoryDto? = try {
                            Gson().fromJson(receivedText, CourierLocationHistoryDto::class.java)
                        } catch (e: Exception) {
                            null
                        }
                        if (location != null) {
                            CourierLocationHistoryService.add(
                                location.copy(
                                    merchantId = merchantId,
                                    staffId = staffId,
                                    time = Timestamp(System.currentTimeMillis())
                                )
                            )
                            val adminChannel =
                                CourierSocketService.adminConnections.filter { it.merchantId == merchantId }

                            for(channel in adminChannel){
                                if (channel?.session != null) {
                                    channel.session?.send(receivedText)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                    CourierSocketService.connections.removeIf { it.session == this }
                }
            }
        }
        authenticate("operator", "merchant","staff") {
            webSocket("admin") {
                try {
                    val staffP = call.principal<StaffPrincipal>()
                    val merchantP = call.principal<MerchantPrincipal>()
                    var operatorId: Long? = null
                    var merchantId: Long? = null
                    if (staffP != null) {
                        merchantId = staffP.merchantId?.toLong()
                        operatorId = staffP.staffId
                    } else {
                        merchantId = merchantP?.merchantId?.toLong()
                    }
                    val connection =
                        CourierSocketService.adminConnections.find { it.merchantId == merchantId && it.operatorId == operatorId }
                    if (connection == null) {
                        CourierSocketService.adminConnections += AdminConnection(
                            merchantId = merchantId,
                            operatorId = operatorId,
                            session = this,
                            connectAt = Timestamp(System.currentTimeMillis())
                        )
                    }

                    for (frame in incoming) {
//                        frame as? Frame.Text ?: continue
//                        val receivedText = frame.readText()
//                        val admin: AdminConnection? = try {
//                            Gson().fromJson(receivedText, AdminConnection::class.java)
//                        } catch (e: Exception) {
//                            null
//                        }
//                        val connection =
//                            CourierSocketService.adminConnections.find { it.merchantId == merchantId && it.operatorId == operatorId }
//                        if (admin != null) {
//
//                        }
                    }
                    try {
                        while (isActive) {
                            // Just keep the WebSocket open
                        }
                    } finally {
                        println("Admin disconnected")
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                    CourierSocketService.connections.removeIf { it.session == this }
                }

            }
        }

        authenticate("staff", "operator") {

            webSocket("mchat") {
                try {
                    val principal = call.principal<StaffPrincipal>()
                    val sender: Sender?
                    val fromId: Long?
                    var operatorId: Long? = null
                    val connection: ChatConnections?
                    val merchantId = principal?.merchantId
                    val staffId = principal?.staffId
                    val staff = StaffService.get(staffId, merchantId)
                    println(" connections  ${ChatMessageService.chatConnections}")
                    if (staff?.position == "courier") {
                        fromId = staffId
                        sender = Sender.COURIER
                        connection = ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender }
                    } else {
                        fromId = merchantId
                        sender = Sender.MERCHANT
                        operatorId = staffId
                        connection =
                            ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender && it.operatorId == operatorId }
                    }
                    if (connection == null) {
                        println("connection null")
                        val getter = if (sender == Sender.MERCHANT) {
                            Sender.COURIER
                        } else {
                            Sender.MERCHANT
                        }
                        val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(fromId, getter)
                        if (notReadMsgInfo.isNotEmpty()) {
                            this.send(Gson().toJson(notReadMsgInfo))
                            ChatMessageRepository.readMessages(fromId, getter)
                        }
                        ChatMessageService.chatConnections += ChatConnections(
                            id = fromId,
                            sender = sender,
                            operatorId = operatorId,
                            connectAt = Timestamp(System.currentTimeMillis()),
                            session = this
                        )
                        println(ChatMessageService.chatConnections.toString())
                    }
                    var toId = if (sender == Sender.COURIER) {
                        principal?.merchantId
                    } else {
                        null
                    }
                    for (frame in incoming) {
                        val connection =
                            ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender && it.operatorId == operatorId }
                        println(connection.toString())
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        println(receivedText)
                        val chatMessage: ChatMessageDto? = Gson().fromJson(receivedText, ChatMessageDto::class.java)
                        toId = if (sender == Sender.MERCHANT) chatMessage?.toId else toId

                        println(chatMessage.toString())

                        if (connection?.session != null) {
                            println(connection.session)
                            ChatMessageService.sendMessage(
                                toId,
                                ChatMessageSaveDto(
                                    fromId = fromId,
                                    toId = toId,
                                    operatorId = operatorId,
                                    sender = sender,
                                    time = Timestamp(System.currentTimeMillis()),
                                    message = chatMessage?.message
                                )
                            )
                        } else {

                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    ChatMessageService.chatConnections.removeIf { it.session == this }
                    println("inside finally ${ChatMessageService.chatConnections}")
                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                    println(ChatMessageService.chatConnections)
                }
            }

            webSocket("online_pbx") {

                send(
                    Gson().toJson(
                        MessageModel(
                            message = StatusConnection.CONNECTED.value
                        )
                    )
                )

                try {

                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()

                        val socketEntity: SocketEntity? = try {
                            Gson().fromJson(receivedText, SocketEntity::class.java)
                        } catch (e: Exception) {
                            null
                        }

                        if (socketEntity != null) {
                            SocketService.connections.removeIf { it.phone == socketEntity.phone }
                            val isAdded = SocketService.connect(
                                socketEntity.copy(session = this)
                            )
                        }

                        println("receivedText: $receivedText")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // закрытие сокета и удаление связанной с ним информации
                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                    SocketService.connections.removeIf { it.session == this }
                }
            }
        }



    }
}