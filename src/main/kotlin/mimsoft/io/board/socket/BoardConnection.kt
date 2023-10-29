package mimsoft.io.board.socket

import io.ktor.websocket.*

data class BoardConnection(
  val branchId: Long? = null,
  val boardId: Long? = null,
  val merchantId: Long? = null,
  var session: DefaultWebSocketSession? = null,
) {}
