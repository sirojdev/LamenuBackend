package mimsoft.io.board.socket

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import mimsoft.io.board.auth.BoardAuthService
import mimsoft.io.features.order.Order
import java.util.*

object BoardSocketService {
     val boardConnections: MutableSet<BoardConnection> = Collections.synchronizedSet(LinkedHashSet())
    fun setBoardConnection(
        boardId: Long?,
        branchId: Long?,
        merchantId: Long?,
        defaultWebSocketServerSession: DefaultWebSocketServerSession
    ) {
        val connection =
            boardConnections.find { it.boardId == boardId && it.branchId == branchId && it.merchantId == merchantId }
        if (connection == null) {
            boardConnections += BoardConnection(
                branchId = branchId,
                boardId = boardId,
                merchantId = merchantId,
                session = defaultWebSocketServerSession
            )
        }
    }

    suspend fun sendOrderToBoard(order: Order, type: BoardOrderStatus, action: Action) {
        val branchId = order.branch?.id
        val merchantId = order.merchant?.id
        val dto = BoardAuthService.getBoardId(branchId = branchId, merchantId = merchantId)
        val connection =
            boardConnections.find { it.boardId == dto?.id && it.branchId == branchId && it.merchantId == merchantId }
        connection?.session?.send(Gson().toJson(SocketOrderResponseDto(order = order,type = type, action = action)))
    }
}