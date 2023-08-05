package mimsoft.io.utils.plugins

import com.google.gson.Gson
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mimsoft.io.courier.location.AdminConnection
import mimsoft.io.courier.location.Connection
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.services.socket.MessageModel
import mimsoft.io.services.socket.SocketEntity
import mimsoft.io.services.socket.SocketService
import mimsoft.io.services.socket.StatusConnection
import okhttp3.internal.wait
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureSocket() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {

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
                           val adminChannel= CourierSocketService.adminConnections.find { it.merchantId == location.merchantId }
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
            try{
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
            }catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                CourierSocketService.connections.removeIf { it.session == this }
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