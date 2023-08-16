package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.utils.principal.BasePrincipal
import java.sql.Timestamp

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
                    OperatorSocketService.findConnection(staffId, merchantId, uuid,this)

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
             * YANGI ORDERLARNI COURIER GA BERIB YUBORADI COURIER ONLY LISTEN
             * */
            webSocket("newOrder") {
                try {
                    val principal = call.principal<BasePrincipal>()
                    val staffId = principal?.staffId
                    val merchantId = principal?.merchantId
                    val uuid = principal?.uuid
                    /**
                     * AGAR CONNECTION BOLMASA YANGI CONNECTION QO'SHADI
                     * OPERATOR FAQAT ESHITIB TURADI
                     * */
                    CourierSocketService.findCourierListenNewOrder(staffId, merchantId, uuid,this)

                    for (frame in incoming) {
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
                    CourierSocketService.locationConnection.removeIf { it.session == this }
                }
            }
        }
    }
}