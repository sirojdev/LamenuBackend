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
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
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

    suspend fun sendOrderToOperators(order: OrderWrapper) {
        operatorConnections.forEach { operatorConnection ->
            if (operatorConnection.session != null) {
                operatorConnection.session?.send(Gson().toJson(order))
            }
        }
    }

    suspend fun sendBookingToOperators(book: BookDto) {
        operatorConnections.forEach { operatorConnection ->
            if (operatorConnection.session != null) {
                operatorConnection.session?.send(Gson().toJson(book))
            }
        }
    }
    suspend fun findNearCourierAndSendOrderToCourier(order: OrderWrapper, offset: Int) {
        val courierIdList = CourierSocketService.courierIdList(order.order?.merchantId)
        if (courierIdList.isNotEmpty()) {
            val courier = CourierService.findNearCourier(order.order?.branch?.id, offset, courierIdList)
            if (courier != null) {
                println(courier)
                if (CourierSocketService.courierNewOrderConnection.isNotEmpty()) {
                    val connection =
                        CourierSocketService.courierNewOrderConnection.find { it.staffId == courier.staffId }
                    if (connection?.session != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            connection.session!!.send(Gson().toJson(order))
                        }
                        OrderRepositoryImpl.updateOnWave(orderId = order.order?.id!!, true)
                        sendOrderList.add(
                            SenderOrdersToCourierDto(
                                courierId = courier.staffId!!,
                                orderId = order.order?.id!!,
                                time = Timestamp(System.currentTimeMillis()),
                                offset = offset
                            )
                        )
                        waitAnswer(40000, order.order.id, connection.staffId, offset, order)
                    } else {
                        findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
                    }
                } else {
                    OrderRepositoryImpl.updateOnWave(orderId = order.order?.id!!, false)
                }
            } else {
                OrderRepositoryImpl.updateOnWave(orderId = order.order?.id!!, false)
            }
        }
    }

    private fun waitAnswer(time: Long, orderId: Long, staffId: Long?, offset: Int?, order: OrderWrapper) {
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