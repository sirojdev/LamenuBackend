package mimsoft.io.courier.location

import io.ktor.websocket.*
import mimsoft.io.courier.merchantChat.Sender
import java.sql.Timestamp

data class Connection(
    val merchantId: Long? = null,
    val staffId: Long? = null,
    val connectAt: Timestamp? = null,
    val uuid: String? = null,
    var session: DefaultWebSocketSession? = null,
)

data class AdminConnection(
    var operatorId: Long? = null,
    var merchantId: Long? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null,
)

data class ChatConnections(
    val id: Long? = null,
    val operatorId: Long? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null,
    var sender: Sender? = null
)

data class CourierConnection(
    val id: Long? = null,
    val staffId: Long? = null,
    var merchantId: Long? = null,
    var uuid: String? = null,
    val connectAt: Timestamp? = null,
    var session: DefaultWebSocketSession? = null
)