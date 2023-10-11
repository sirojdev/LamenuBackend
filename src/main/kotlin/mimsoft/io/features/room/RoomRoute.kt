package mimsoft.io.features.room

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToRoom() {
    val roomService: RoomRepository = RoomService
    val roomMapper = RoomMapper

    get("rooms") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val rooms = roomService.getAll(merchantId = merchantId, branchId = branchId)
        if (rooms.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(rooms)
    }

    get("room/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val room = roomService.get(id=id, merchantId=merchantId, branchId = branchId)
        if (room == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(room)
    }

    post("room") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val room = call.receive<RoomDto>()
        roomService.add(roomMapper.toRoomTable(room.copy(merchantId = merchantId, branchId = branchId)))
        call.respond(HttpStatusCode.OK)
    }

    put("room") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val room = call.receive<RoomDto>()
        roomService.update(room.copy(merchantId=merchantId, branchId = branchId))
        call.respond(HttpStatusCode.OK)
    }

    delete("room/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val response = roomService.delete(id=id, merchantId=merchantId, branchId = branchId)
        call.respond(response)
    }
}