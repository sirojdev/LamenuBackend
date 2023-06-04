package mimsoft.io.features.room

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToRoom() {
    val roomService: RoomRepository = RoomService
    val roomMapper = RoomMapper
    get("rooms") {
        val rooms = roomService.getAll().map { roomMapper.toRoomDto(it) }
        if (rooms.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(rooms)
    }

    get("room/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val room = roomMapper.toRoomDto(roomService.get(id))
        if (room == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(room)
    }

    post("room") {
        val room = call.receive<RoomDto>()
        roomService.add(roomMapper.toRoomTable(room))
        call.respond(HttpStatusCode.OK)
    }

    put("room") {
        val room = call.receive<RoomDto>()
        roomService.update(roomMapper.toRoomTable(room))
        call.respond(HttpStatusCode.OK)
    }

    delete("room") {
        val roomDto = call.receive<RoomDto>()
        val id = roomDto.id
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        roomService.delete(id)
        call.respond(HttpStatusCode.OK)
    }


}