package mimsoft.io.courier.location

import io.ktor.server.websocket.*
import java.sql.Timestamp
import java.util.*

object CourierSocketService {
    val locationConnection: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())

    val adminConnections: MutableSet<AdminConnection> = Collections.synchronizedSet(LinkedHashSet())
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