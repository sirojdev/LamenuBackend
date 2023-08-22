package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.toOperatorSocket() {
    route("operator") {

        authenticate("operator") {
            /**
             * YANGI ORDER KELIB TUSHSA SHUNI OPERATORGA JONATISH UCHUN WEBSOCKET
             * */
            webSocket("order") {
                try {
                    val principal = call.principal<BasePrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    val uuid = principal?.uuid
                    /**
                     * AGAR CONNECTION BOLMASA YANGI CONNECTION QO'SHADI
                     * OPERATOR FAQAT ESHITIB TURADI
                     * */
                    OperatorSocketService.findConnection(staffId, merchantId, uuid, this)

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
                    close(CloseReason(CloseReason.Codes.NORMAL, "Connection closed"))
                    CourierSocketService.locationConnection.removeIf { it.session == this }
                }
            }
        }
        authenticate("staff") {
            /**
             * YANGI ORDERLARNI COURIER GA BERIB YUBORADI COURIER LISTEN AND GIVE ANSWER ACCEPTED AND NOT_ACCEPTED
             * */
            webSocket("courier") {

                    val principal = call.principal<BasePrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    val uuid = principal?.uuid
                try {
                    /**
                     * AGAR CONNECTION BOLMASA YANGI CONNECTION QO'SHADI
                     * OPERATOR FAQAT ESHITIB TURADI
                     * */
                    CourierSocketService.findCourierListenNewOrder(staffId, merchantId, uuid, this)

                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val response = Gson().fromJson(receivedText, AcceptedDto::class.java)
                        if (response != null) {
                            if (response.status == "ACCEPTED") {
                                val dto = OperatorSocketService.sendOrderList.find {
                                    it.courierId == staffId && response.orderId == it.orderId
                                }
                                if (dto != null) {
                                    OperatorSocketService.sendOrderList.removeIf {
                                        it.courierId == staffId && response.orderId == it.orderId
                                    }
                                    CourierOrderService.joinOrderToCourier(
                                            courierId = dto.courierId,
                                    orderId = dto.orderId,
                                    )
//                                    OrderRepositoryImpl.updateOnWave(dto.orderId,true)
                                }
                            } else if (response.status == "NOT_ACCEPTED") {
                                val dto = OperatorSocketService.sendOrderList.find {
                                    it.courierId == staffId && response.orderId == it.orderId
                                }
                                if (dto != null) {
                                    OperatorSocketService.sendOrderList.removeIf {
                                        it.courierId == staffId && response.orderId == it.orderId
                                    }
                                    OperatorSocketService.findNearCourierAndSendOrderToCourier(
                                        OrderRepositoryImpl.get(
                                            response.orderId,
                                            merchantId = merchantId
                                        ), dto.offset!!+1
                                    )
                                }
                            }
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
                    CourierService.updateIsActive(staffId, false)
                    close(CloseReason(CloseReason.Codes.NORMAL, "Connection closed"))
                    CourierSocketService.locationConnection.removeIf { it.session == this  }
                }
            }
        }
    }
}