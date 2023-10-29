package mimsoft.io.board.socket

import io.ktor.http.*
import io.ktor.server.websocket.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BoardSocketServiceTest {

  @Test fun getBoardConnections() {}

  @Test
  fun setBoardConnection(
    boardId: Long?,
    branchId: Long?,
    merchantId: Long?,
    defaultWebSocketServerSession: DefaultWebSocketServerSession
  ) {
    assertEquals(HttpStatusCode.OK, 403)
  }

  @Test fun sendOrderToBoard() {}
}
