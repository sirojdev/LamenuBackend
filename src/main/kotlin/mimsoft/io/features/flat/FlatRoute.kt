package mimsoft.io.features.flat

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToFlat(){
    val flatService : FlatRepository = FlatService
    val flatMapper = FlatMapper
    get("flats"){
        val flats = flatService.getAll().map { flatMapper.toFlatDto(it) }
        if(flats.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(flats)
    }

    get("flat/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val flat = flatMapper.toFlatDto(flatService.get(id))
        if(flat == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(flat)
    }

    post ("flat"){
        val flat = call.receive<FlatDto>()
        flatService.add(flatMapper.toFlatTable(flat))
        call.respond(HttpStatusCode.OK)
    }

    put ("flat"){
        val flat = call.receive<FlatDto>()
        flatService.update(flatMapper.toFlatTable(flat))
        call.respond(HttpStatusCode.OK)
    }

    delete("flat"){
        val flat = call.receive<FlatDto>()
        val id = flat.id
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete        }
        flatService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}