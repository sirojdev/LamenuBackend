package mimsoft.io.board.order

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import mimsoft.io.board.socket.BoardResponseModel
import mimsoft.io.features.order.OrderService
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToBoardOrder() {
    route("order") {
        get() {
            val pr = getPrincipal()
            val branchId = pr?.branchId
            val merchantId = pr?.merchantId
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

            call.respond(Gson().toJson(BoardResponseModel(inProgress = oneList, ready = twoList)))
        }
    }
}