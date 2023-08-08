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
        routeToMerchantChat()
        webSocket("location") {
            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    println(receivedText)
                    val location: CourierLocationHistoryDto? = try {
                        Gson().fromJson(receivedText, CourierLocationHistoryDto::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    CourierSocketService.connections.removeIf { it.staffId == location?.staffId }
                    if (location != null) {
                        val isExist = CourierSocketService.connect(
                            Connection(
                                staffId = location.staffId,
                                session = this,
                                merchantId = location.merchantId
                            )
                        )
                        if (isExist) {
                            val adminChannel =
                                CourierSocketService.adminConnections.find { it.merchantId == location.merchantId }
                            if (adminChannel?.session != null) {
                                adminChannel.session?.send(receivedText)
                            }
                            CourierLocationHistoryService.add(location)
                        } else {
                            this.close()
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
        webSocket("admin") {
            try {
                send("admin connected")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val admin: AdminConnection? = try {
                        Gson().fromJson(receivedText, AdminConnection::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    CourierSocketService.adminConnections.removeIf { it.merchantId == admin?.merchantId }
                    if (admin != null) {
                        val isExist = CourierSocketService.adminConnect(
                            AdminConnection(
                                session = this,
                                merchantId = admin.merchantId
                            )
                        )
                        if (!isExist) {
                            this.close()
                        }
                    }
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
//        authenticate("staff", "merchant") {
//            webSocket("mchat") {
//                val merchantPrincipal = call.principal<MerchantPrincipal>()
//                val courierPrincipal = call.principal<StaffPrincipal>()
//                val chatMessage: ChatMessageDto? = receiveDeserialized<ChatMessageDto?>()
//                val sender: Sender?
//                val fromId: Long?
//                val toId: Long?
//                // is courier
//                if (merchantPrincipal == null) {
//                    fromId = courierPrincipal?.staffId
//                    sender = Sender.COURIER
//                    toId = courierPrincipal?.merchantId
//                } else {
//                    fromId = merchantPrincipal.merchantId
//                    sender = Sender.MERCHANT
//                    toId = chatMessage?.toId
//                }
//
//                try {
//                    val connection =
//                        ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender }
//                    if (connection?.session != null ) {
//                        ChatMessageService.sendMessage(
//                            toId, ChatMessageSaveDto(
//                                fromId = fromId,
//                                toId = toId,
//                                sender = sender,
//                                time = Timestamp(System.currentTimeMillis()),
//                                message = chatMessage?.message
//                            )
//                        )
//                    } else {
//                        val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(fromId)
//                        if (notReadMsgInfo != null) {
//                            if (notReadMsgInfo.isNotEmpty()) {
//                                this.send(Gson().toJson(notReadMsgInfo))
//                                ChatMessageRepository.readMessages(fromId, sender)
//                            }
//                        }
////                        ChatMessageService.sendMessage(
////                            toId, ChatMessageSaveDto(
////                                fromId = fromId,
////                                toId = toId,
////                                sender = sender,
////                                time = Timestamp(System.currentTimeMillis()),
////                                message = chatMessage?.message
////                            )
////                        )
//                        ChatMessageService.chatConnections += ChatConnections(
//                            id = fromId,
//                            sender = sender,
//                            connectAt = Timestamp(System.currentTimeMillis()),
//                            session = this
//                        )
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
//                    SocketService.connections.removeIf { it.session == this }
//                }
//            }
//        }

        authenticate("staff", "merchant") {
            webSocket("mchat") {
                try {
                    val merchantPrincipal = call.principal<MerchantPrincipal>()
                    val courierPrincipal = call.principal<StaffPrincipal>()

                    val sender: Sender?
                    val fromId: Long?
                    if (merchantPrincipal == null) {
                        LOGGER.info("entity {}", courierPrincipal)
                        fromId = courierPrincipal?.staffId
                        sender = Sender.COURIER
                    } else {
                        fromId = merchantPrincipal.merchantId
                        sender = Sender.MERCHANT
                    }
                    println("sender   $sender")
                    println("from id $fromId")
                    println(ChatMessageService.chatConnections)
                    val connection = ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender }
                    if (connection == null) {
                        println("connection null")
                        val getter = if (sender == Sender.MERCHANT) {
                            Sender.COURIER
                        } else {
                            Sender.MERCHANT
                        }
                        val notReadMsgInfo = ChatMessageRepository.getNotReadMessagesInfo(fromId, getter)
                        if (notReadMsgInfo != null) {
                            if (notReadMsgInfo.isNotEmpty()) {
                                this.send(Gson().toJson(notReadMsgInfo))
                                ChatMessageRepository.readMessages(fromId, getter)
                            }
                        }
                        ChatMessageService.chatConnections += ChatConnections(
                            id = fromId,
                            sender = sender,
                            connectAt = Timestamp(System.currentTimeMillis()),
                            session = this
                        )
                        println(ChatMessageService.chatConnections.toString())
                    }
                    var toId = if (sender == Sender.COURIER) {
                        courierPrincipal?.merchantId
                    } else {
                        merchantPrincipal?.merchantId
                    }
                    for (frame in incoming) {
                        val connection =
                            ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender }
                        println(connection.toString())
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        println(receivedText)
                        val chatMessage: ChatMessageDto? = Gson().fromJson(receivedText, ChatMessageDto::class.java)
                        toId = if (sender == Sender.MERCHANT) chatMessage?.toId else toId

                        println(chatMessage.toString())

                        println(toId)
                        if (connection?.session != null) {
                            println(connection.session)
                            ChatMessageService.sendMessage(
                                toId, ChatMessageSaveDto(
                                    fromId = fromId,
                                    toId = toId,
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
                    ChatMessageService.chatConnections.removeIf { it.session ==this }
                    println("inside finally ${ChatMessageService.chatConnections}")
                    close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                    println(ChatMessageService.chatConnections)
                }
            }
        }


        webSocket("api/v1/ws") {

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