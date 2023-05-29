package mimsoft.io.room

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToRoom(){
    val roomService : RoomService = RoomServiceImpl
    
    get("rooms"){
        val rooms = roomService.getAll()
        if(rooms.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(rooms)
    }

    get("room/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val room = roomService.get(id)
        if(room == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(room)
    }

    post ("room"){
        val room = call.receive<RoomDto>()
        roomService.add(room)
        call.respond(HttpStatusCode.OK)
    }

    put ("room"){
        val room = call.receive<RoomDto>()
        roomService.update(room)
        call.respond(HttpStatusCode.OK)
    }

    delete("room/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        roomService.delete(id)
        call.respond(HttpStatusCode.OK)
    }

}