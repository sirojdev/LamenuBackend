package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mimsoft.io.courier.CourierSocketService
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import java.util.*
import java.sql.Timestamp

object OperatorSocketService {
    val operatorConnections: MutableSet<OperatorConnection> = Collections.synchronizedSet(LinkedHashSet())
    val sendOrderList: MutableSet<SenderOrdersToCourierDto> = Collections.synchronizedSet(LinkedHashSet())


    fun setConnection(
        staffId: Long?,
        merchantId: Long?,
        uuid: String?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ): OperatorConnection {
        val connection =
            operatorConnections.find { it.deviceUUid == uuid && it.staffId == staffId && it.merchantId == merchantId }
        return if (connection == null) {
            val conn = OperatorConnection(
                staffId = staffId,
                deviceUUid = uuid,
                merchantId = merchantId,
                session = defaultWebSocketServerSession
            )
            operatorConnections +=conn
            conn
        } else {
            connection
        }
    }


    suspend fun sendBookingToOperators(book: BookDto) {
        operatorConnections.forEach { operatorConnection ->
            if (operatorConnection.session != null) {
                val socketData = SocketData(type = SocketType.BOOK, data = Gson().toJson(book))
                operatorConnection.session?.send(Gson().toJson(socketData))
            }
        }
    }

    suspend fun findNearCourierAndSendOrderToCourier(order: Order, offset: Int) {
        val courierIdList = CourierSocketService.courierIdList(order.merchant?.id)
        if (courierIdList.isNotEmpty()) {
            val courier = CourierService.findNearCourier(order.branch?.id, offset, courierIdList)
            if (courier != null) {
                println(courier)
                if (CourierSocketService.courierConnections.isNotEmpty()) {
                    val connection =
                        CourierSocketService.courierConnections.find { it.staffId == courier.staffId }
                    if (connection?.session != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val socketDto = SocketData(data = Gson().toJson(order), type = SocketType.ORDER)
                            connection.session!!.send(Gson().toJson(socketDto))
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
                        waitAnswer(150000, order.id, connection.staffId, offset, order)
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

    suspend fun acceptedOrder(response: AcceptedDto, staffId: Long?) {
        val dto = sendOrderList.find {
            it.courierId == staffId && response.orderId == it.orderId
        }
        if (dto != null) {
            sendOrderList.removeIf {
                it.courierId == staffId && response.orderId == it.orderId
            }
            CourierOrderService.joinOrderToCourier(
                courierId = dto.courierId,
                orderId = dto.orderId,
            )
//                                    OrderRepositoryImpl.updateOnWave(dto.orderId,true)
        }
    }

    suspend fun notAccepted(response: AcceptedDto, staffId: Long?) {
        val dto = sendOrderList.find {
            it.courierId == staffId && response.orderId == it.orderId
        }
        if (dto != null) {
            sendOrderList.removeIf {
                it.courierId == staffId && response.orderId == it.orderId
            }
            findNearCourierAndSendOrderToCourier(
                OrderService.get(
                    response.orderId
                ).body as Order,
                dto.offset!! + 1
            )
        }

    }
}
