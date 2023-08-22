package mimsoft.io.courier.location

import io.ktor.server.websocket.*
import mimsoft.io.features.courier.CourierService
import java.sql.Timestamp
import java.util.*

object CourierSocketService {
    val locationConnection: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())

    val adminConnections: MutableSet<AdminConnection> = Collections.synchronizedSet(LinkedHashSet())
    val courierNewOrderConnection: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())
    fun findConnection(
        staffId: Long?,
        merchantId: Long?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        val connection = locationConnection.find { it.staffId == staffId }
        if (connection == null) {
            locationConnection += Connection(
                staffId = staffId,
                session = defaultWebSocketServerSession,
                merchantId = merchantId,
                connectAt = Timestamp(System.currentTimeMillis())
            )
        }
    }

    /**
     * YANGI COURIER QOSHILGANDA CONNECTIONLARGA QOSHADI VA COURIER ISACTIVE NI TRUE QILIB QOYADI
     * */
    suspend fun findCourierListenNewOrder(
        staffId: Long?,
        merchantId: Long?,
        uuid: String?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        val connection = courierNewOrderConnection.find { it.staffId == staffId && it.uuid == uuid }
        if (connection == null) {
            courierNewOrderConnection += Connection(
                staffId = staffId,
                uuid = uuid,
                session = defaultWebSocketServerSession,
                merchantId = merchantId,
                connectAt = Timestamp(System.currentTimeMillis())
            )
            CourierService.updateIsActive(staffId, true)
        }
    }

    fun findAdminConnection(
        merchantId: Long?,
        operatorId: Long?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        val connection = adminConnections.find { it.merchantId == merchantId && it.operatorId == operatorId }
        if (connection == null) {
            adminConnections += AdminConnection(
                merchantId = merchantId,
                operatorId = operatorId,
                session = defaultWebSocketServerSession,
                connectAt = Timestamp(System.currentTimeMillis())
            )
        }
    }


}