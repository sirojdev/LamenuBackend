package mimsoft.io.board.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.client.device.DeviceType
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToBoardAuth() {
    authenticate("board-device") {
        post("sign-in") {

            val pr = call.principal<BoardDevicePrincipal>()
            val authDto = call.receive<BoardAuthDto>()
            call.respond(BoardAuthService.signIn(authDto.copy(branchId = pr?.branchId, merchantId = pr?.merchantId)))
        }
    }

    post("sign-up") {
        val boardAuthDto = call.receive<BoardAuthDto>()
        call.respond(BoardAuthService.singUp(boardAuthDto))
    }
    post("device") {
        val device: BoardDeviceModel = call.receive()
        if (device.brand == null || device.model == null || device.build == null
            || device.uuid.isNullOrBlank() || device.merchantId == null || device.branchId == null
        ) {
            call.respond(HttpStatusCode.BadRequest, "error input")
        } else {
            val ip = call.request.host()
            val result = DeviceController.boardAuth(device.copy(ip = ip, deviceType = DeviceType.BOARD))
            call.respond(result)
        }
    }
}