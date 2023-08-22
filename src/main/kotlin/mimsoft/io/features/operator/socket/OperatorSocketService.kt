package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import java.util.*
import java.sql.Timestamp

object OperatorSocketService {
    val operatorConnections: MutableSet<OperatorConnection> = Collections.synchronizedSet(LinkedHashSet())
    val sendOrderList: MutableSet<SenderOrdersToCourierDto> = Collections.synchronizedSet(LinkedHashSet())


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
    suspend fun findNearCourierAndSendOrderToCourier(order: Order, offset: Int) {
        val courierIdList = CourierSocketService.courierIdList(order.merchant?.id)
        if (courierIdList.isNotEmpty()) {
            val courier = CourierService.findNearCourier(order.branch?.id, offset, courierIdList)
            if (courier != null) {
                println(courier)
                if (CourierSocketService.courierNewOrderConnection.isNotEmpty()) {
                    val connection =
                        CourierSocketService.courierNewOrderConnection.find { it.staffId == courier.staffId }
                    if (connection?.session != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            connection.session!!.send(Gson().toJson(order))
                        }
                        OrderService.updateOnWave(orderId = order.id!!, true)
                        sendOrderList.add(
                            SenderOrdersToCourierDto(
                                courierId = courier.staffId!!,
                                orderId = order.id,
                                time = Timestamp(System.currentTimeMillis()),
                                offset = offset
                            )
                        )
                        waitAnswer(40000, order.id, connection.staffId, offset, order)
                    } else {
                        findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
                    }
                } else {
                    OrderService.updateOnWave(orderId = order.id!!, false)
                }
            } else {
                OrderService.updateOnWave(orderId = order.id!!, false)
            }
        }
    }

    private fun waitAnswer(time: Long, orderId: Long, staffId: Long?, offset: Int?, order: Order) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(time) // 15 seconds timeout
            val sendOrdersToCourierDto = sendOrderList.find {
                it.orderId == orderId && it.courierId == staffId
            }
            if (sendOrdersToCourierDto != null) {
                sendOrderList.removeIf {
                    it.orderId == orderId && it.courierId == staffId
                }
                findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
            }
        }
    }
}
