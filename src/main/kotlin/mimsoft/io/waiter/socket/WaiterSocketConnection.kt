package mimsoft.io.waiter.socket

import io.ktor.websocket.*
import java.sql.Timestamp

data class WaiterSocketConnection(
  val id: Long? = null,
  val staffId: Long? = null,
  var merchantId: Long? = null,
  var uuid: String? = null,
  val connectAt: Timestamp? = null,
  var session: DefaultWebSocketSession? = null
)
