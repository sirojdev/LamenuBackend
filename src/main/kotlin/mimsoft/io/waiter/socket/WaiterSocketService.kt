package mimsoft.io.waiter.socket

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
import mimsoft.io.features.operator.socket.AcceptedDto
import mimsoft.io.features.operator.socket.OrderCourierDto
import mimsoft.io.features.operator.socket.SenderOrdersToCourierDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.sql.Timestamp

object WaiterSocketService {
    val log: Logger = LoggerFactory.getLogger(WaiterSocketService::class.java)
    val waiterSocketService: MutableSet<WaiterSocketConnection> = Collections.synchronizedSet(LinkedHashSet())
    fun setConnection(
        staffId: Long?,
        merchantId: Long?,
        uuid: String?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ): WaiterSocketConnection {
        return waiterSocketService.find { it.uuid == uuid && it.staffId == staffId && it.merchantId == merchantId }
            ?: WaiterSocketConnection(
                staffId = staffId,
                uuid = uuid,
                merchantId = merchantId,
                session = defaultWebSocketServerSession
            )
    }
}
