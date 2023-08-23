package mimsoft.io.board.socket

import io.ktor.server.websocket.*
import mimsoft.io.features.operator.socket.OperatorConnection
import mimsoft.io.features.operator.socket.SenderOrdersToCourierDto
import java.util.*

object BoardSocketService {
    val boardConnections: MutableSet<BoardConnection> = Collections.synchronizedSet(LinkedHashSet())
    fun findBoardConnection(
        boardId:Long?,
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
}