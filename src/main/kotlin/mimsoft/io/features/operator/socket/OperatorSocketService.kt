package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.Order
import java.util.*

object OperatorSocketService {
    val operatorConnections: MutableSet<OperatorConnection> = Collections.synchronizedSet(LinkedHashSet())


    fun findConnection(
        staffId: Long?,
        merchantId: Long?,
        uuid: String?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        val connection =
            operatorConnections.find { it.deviceUUid == uuid && it.staffId == staffId && it.merchantId == merchantId }
        if (connection == null) {
            operatorConnections += OperatorConnection(
                staffId = staffId,
                deviceUUid = uuid,
                merchantId = merchantId,
                session = defaultWebSocketServerSession
            )
        }
    }

    suspend fun sendBookingToOperators(book: BookDto) {
        operatorConnections.forEach { operatorConnection ->
            if (operatorConnection.session != null) {
                operatorConnection.session?.send(Gson().toJson(book))
            }
        }
    }

    suspend fun findNearCourierAndSendOrderToCourier(order: Order, offset:Int?) {
        val courier = CourierService.findNearCourier(order.branch?.id,0)
        if(courier!=null){
            if(CourierSocketService.courierNewOrder.isNotEmpty()){
                val connection = CourierSocketService.courierNewOrder.find { it.staffId == courier?.staffId }
                if (connection?.session != null) {
                    connection.session?.send(Gson().toJson(order))
                } else {
                    findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
                }
            }
        }
    }
}