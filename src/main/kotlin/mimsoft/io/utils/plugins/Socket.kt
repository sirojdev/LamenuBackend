package mimsoft.io.utils.plugins

import com.google.gson.Gson
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import mimsoft.io.courier.location.ChatConnections
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.courier.merchantChat.*
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.staff.StaffService
import mimsoft.io.services.socket.MessageModel
import mimsoft.io.services.socket.SocketEntity
import mimsoft.io.services.socket.SocketService
import mimsoft.io.services.socket.StatusConnection
import mimsoft.io.utils.principal.BasePrincipal
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
            /**
             * COURIER locationlarini jo'natib turishi uchun WebSocket
             * */
            webSocket("location") {
                try {
                    val principal = call.principal<StaffPrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    /**
                     * AGAR CONNECTION BOLMASA YANGI CONNECTION QO'SHADI
                     * */
                    CourierSocketService.findConnection(staffId, merchantId, this)

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
                            /**
                             * ULANGAN BARCHA OPERATORLARGA COURIER NI LOCATION JUNATISH UCHUN ADMIN CHANNEL NI OLADI.
                             * */
                            val adminChannel =
                                CourierSocketService.adminConnections.filter { it.merchantId == merchantId }

                            for (channel in adminChannel) {
                                if (channel.session != null) {
                                    channel.session?.send(receivedText)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Connection closed"))
                    CourierSocketService.locationConnection.removeIf { it.session == this }
                }
            }
        }
        /**
         * ADMIN ning courier ni locationlarini eshitib turishi uchun websocket
         * */
        authenticate("operator", "merchant") {
            webSocket("admin") {
                try {
                    val staffP = call.principal<StaffPrincipal>()
                    val merchantP = call.principal<MerchantPrincipal>()
                    var operatorId: Long? = null
                    val merchantId: Long?
                    if (staffP != null) {
                        merchantId = staffP.merchantId
                        operatorId = staffP.staffId
                    } else {
                        merchantId = merchantP?.merchantId
                    }
                    /**
                     * SHU ID LIK ADMIN OLDIN ULANGANMI YUQMI TEKSHIRADI VA NULL BOLSA CONNECTION LIST GA QOSHIB QOYADI.
                     * */
                    CourierSocketService.findAdminConnection(merchantId, operatorId, this)
                    /**
                     * ADMIN hech narsa qilmaydi faqat courier jo'natgan location larni oladi.
                     * */
                    for (frame in incoming) {
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
                    close(CloseReason(CloseReason.Codes.NORMAL, ""))
                    CourierSocketService.locationConnection.removeIf { it.session == this }
                }

            }
        }

        authenticate("staff", "operator") {
            webSocket("mchat") {
                try {
                    val principal = call.principal<BasePrincipal>()
                    var sender: Sender? = null
                    var fromId: Long? = null
                    var operatorId: Long? = null
                    var connection: ChatConnections? = null
                    val merchantId = principal?.merchantId
                    val staffId = principal?.staffId
                    val staff = StaffService.get(staffId, merchantId)
                    println(" connections  ${ChatMessageService.chatConnections}")
                    when (staff?.position) {
                        "courier" -> {
                            fromId = staffId
                            sender = Sender.COURIER
                            connection =
                                ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender }
                        }

                        "dev" -> {
                            fromId = merchantId
                            sender = Sender.MERCHANT
                            operatorId = staffId
                            connection =
                                ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender && it.operatorId == operatorId }
                        }

                        else -> {
                            this.close(CloseReason(CloseReason.Codes.NORMAL, "position no courier or operator"))
                        }
                    }
                    /**
                     * CONNECT bolgan operator,merchant,courier ga yangi message larni info sini jonatadi
                     * */
                    ChatMessageService.sendNotReadMessageInfo(sender, fromId, connection, operatorId, this)

                    var toId = if (sender == Sender.COURIER) {
                        principal?.merchantId
                    } else {
                        null
                    }
                    /**
                     * CHAT message qabul qilib turadi
                     * * */
                    for (frame in incoming) {
                        val conn =
                            ChatMessageService.chatConnections.find { it.id == fromId && it.sender == sender && it.operatorId == operatorId }
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val chatMessage: ChatMessageDto? = Gson().fromJson(receivedText, ChatMessageDto::class.java)
                        toId = if (sender == Sender.MERCHANT) chatMessage?.toId else toId
                        if (chatMessage!=null){
                            if (conn?.session != null) {
                                ChatMessageService.sendMessage(
                                    toId, ChatMessageSaveDto(
                                        fromId = fromId,
                                        toId = toId,
                                        sender = sender,
                                        time = Timestamp(System.currentTimeMillis()),
                                        message = chatMessage.message
                                    )
                                )
                            }
                        }

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    ChatMessageService.chatConnections.removeIf { it.session == this }
                    close(CloseReason(CloseReason.Codes.NORMAL, ""))
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