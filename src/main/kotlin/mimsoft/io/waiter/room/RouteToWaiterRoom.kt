package mimsoft.io.waiter.room

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.room.RoomMapper
import mimsoft.io.features.room.RoomRepository
import mimsoft.io.features.room.RoomService
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.ResponseData

fun Route.routeToWaiterRoom() {
    val roomService = RoomService
    val roomMapper = RoomMapper

    get("room") {
        val pr = call.principal<BasePrincipal>()
        val roomId = call.parameters["roomId"]?.toLongOrNull()
        val branchId = pr?.branchId
        val tables = roomService.getWithTableForWaiter(roomId=roomId, branchId = branchId)
        val response = ResponseData(data = RoomWithTableDto(roomId = roomId, tables = tables))
        call.respond(response)
    }

    get("room/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val room = roomService.get(id = id, merchantId = merchantId)
        if (room == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(room)
    }
}