package mimsoft.io.board

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.board.auth.BoardAuthDto
import mimsoft.io.board.auth.BoardAuthService
import mimsoft.io.board.order.routeToBoardOrder
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.courier.auth.AuthDto
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToBoard() {
    route("board") {
        post("sign-in") {
            val authDto = call.receive<BoardAuthDto>()
            BoardAuthService.signIn(authDto)
        }
        post("sign-up") {
            val boardAuthDto = call.receive<BoardAuthDto>()
            call.respond(BoardAuthService.singUp(boardAuthDto))
        }
        authenticate ("board"){
            routeToBoardOrder()
        }
    }
}