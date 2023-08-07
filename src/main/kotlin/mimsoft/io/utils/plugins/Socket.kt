package mimsoft.io.utils.plugins

import com.google.gson.Gson
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import mimsoft.io.courier.location.AdminConnection
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.location.Connection
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.ChatMessageService
import mimsoft.io.courier.merchantChat.repository.ChatMessageRepository
import mimsoft.io.courier.merchantChat.routeToMerchantChat
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.services.socket.MessageModel
import mimsoft.io.services.socket.SocketEntity
import mimsoft.io.services.socket.SocketService
import mimsoft.io.services.socket.StatusConnection
import java.sql.Timestamp
import java.time.Duration

fun Application.configureSocket() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
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
        webSocket("mchat/{to}") {
            val to = call.parameters["to"]?.toLongOrNull()
            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val messageDto = Gson().fromJson(receivedText, ChatMessageDto::class.java)
                    val connection =
                        ChatMessageService.chatConnections.find { it.id == messageDto.from && it.type == messageDto.type }
                    if (connection != null && connection.session!!.isActive) {
                        println("connection already exist ${connection.id}  ${connection.type}")
                        ChatMessageService.sendMessage(to, messageDto)
                    } else {
                        println("new connection ${messageDto.from}  ${messageDto.type}")
                        val notReadMsg = ChatMessageRepository.getNotReadMessages(messageDto.from)
                        if (notReadMsg != null) {
                            if (notReadMsg.isNotEmpty()) {
                                this.send(Gson().toJson(notReadMsg))
                                ChatMessageRepository.readMessages(messageDto.from,messageDto.type)
                            }
                        }
                        ChatMessageService.sendMessage(to, messageDto)
                        ChatMessageService.chatConnections += ChatConnections(
                            id = messageDto.from,
                            type = messageDto.type,
                            connectAt = Timestamp(System.currentTimeMillis()),
                            session = this
                        )
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // закрытие сокета и удаление связанной с ним информации
                close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                SocketService.connections.removeIf { it.session == this }
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