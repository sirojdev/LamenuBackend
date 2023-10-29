package mimsoft.io.waiter.socket

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WaiterSocketService {
  val log: Logger = LoggerFactory.getLogger(WaiterSocketService::class.java)
  val waiterSocketService: MutableSet<WaiterSocketConnection> =
    Collections.synchronizedSet(LinkedHashSet())

  fun setConnection(
    staffId: Long?,
    merchantId: Long?,
    uuid: String?,
    defaultWebSocketServerSession: DefaultWebSocketServerSession
  ): WaiterSocketConnection {
    return waiterSocketService.find {
      it.uuid == uuid && it.staffId == staffId && it.merchantId == merchantId
    }
      ?: WaiterSocketConnection(
        staffId = staffId,
        uuid = uuid,
        merchantId = merchantId,
        session = defaultWebSocketServerSession
      )
  }
}
