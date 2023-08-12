package mimsoft.io.courier.location

import io.ktor.websocket.*
import mimsoft.io.courier.merchantChat.Sender
import java.sql.Timestamp

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

data class ChatConnections(
    val id: Long? = null,
    val operatorId:Long? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null,
    var sender: Sender? = null
)