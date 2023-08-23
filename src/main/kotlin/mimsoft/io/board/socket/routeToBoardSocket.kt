package mimsoft.io.board.socket

import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.courier.location.CourierSocketService
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToBoardSocket() {
    authenticate("board") {
        /**
         * ORDERNI STATUSI ALMASHSA BOARD GA JUNATISH UCHUN WEBSOCKET
         * */
        webSocket("board") {
            try {
                val principal = call.principal<BasePrincipal>()
                val boardId = principal?.boardId
                val merchantId = principal?.merchantId
                val branchId = principal?.branchId
                /**
                 * AGAR CONNECTION BOLMASA YANGI CONNECTION QO'SHADI
                 *
                 * */
                BoardSocketService.findBoardConnection(
                    boardId = boardId,
                    merchantId = merchantId,
                    branchId = branchId,
                    defaultWebSocketServerSession = this
                )
                /**
                 * FIND ALL ORDER AND SEND
                 * */
                val oneList = OrderService.getAll(
                    mapOf(
                        "merchantId" to merchantId,
                        "branchId" to branchId,
                        "statuses" to (
                            listOf(
                                OrderStatus.ACCEPTED.name,
                                OrderStatus.COOKING.name,
                                OrderStatus.ONWAVE
                            )
                        )
                    )
                )
                val twoList = OrderService.getAll(
                    mapOf(
                        "merchantId" to merchantId,
                        "branchId" to branchId,
                        "statuses" to (
                            listOf(
                                OrderStatus.ACCEPTED.name,
                                OrderStatus.COOKING.name,
                                OrderStatus.ONWAVE
                            )
                        )
                    )
                )

                this.send(Gson().toJson(BoardResponseModel(inProgress = oneList, ready = twoList)))
                for (frame in incoming) {
                }
                try {
                    while (isActive) {
                        // Just keep the WebSocket open
                    }
                } finally {
                    println("Admin disconnected")
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                close(CloseReason(CloseReason.Codes.NORMAL, "Connection closed"))
                CourierSocketService.locationConnection.removeIf { it.session == this }
            }
        }
    }
}