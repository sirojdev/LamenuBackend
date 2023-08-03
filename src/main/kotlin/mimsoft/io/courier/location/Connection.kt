package mimsoft.io.courier.location

import io.ktor.websocket.*
import java.sql.Timestamp
import java.util.concurrent.atomic.AtomicInteger

data class Connection(
    val merchantId: Long? = null,
    val staffId: Long? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null,
)
data class AdminConnection(
    var merchantId: Long? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null,
)
