package mimsoft.io.utils.plugins

import com.google.gson.Gson
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.courier.location.Connection
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.services.socket.MessageModel
import mimsoft.io.services.socket.SocketEntity
import mimsoft.io.services.socket.SocketService
import mimsoft.io.services.socket.StatusConnection
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureSocket() {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
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
        webSocket("/location") {

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
                            CourierLocationHistoryService.add(location)
                        }else{
                            this.close()
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                close(CloseReason(CloseReason.Codes.NORMAL, "/////////////////////-->Connection closed"))
                connections.removeIf { it.session == this }
            }
        }
    }
}