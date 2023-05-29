package mimsoft.io.entities.poster

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.flat.FlatMapper

fun Route.routeToPoster(){
    val posterMapper = PosterMapper
    get("posters"){
        val posters = PosterService.getAll().map {posterMapper.toPosterDto(it)}
        if(posters.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(posters)
    }

  /*  get("posters"){
        val posters = PosterService.getAll().map {
            if (it != null) {
                posterMapper.toPosterDto(it)
            }
        }
        if(posters.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(posters)
    }*/

    get("poster/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val address = PosterService.get(id)?.let { it1 -> PosterMapper.toPosterDto(it1) }
        if(address == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(address)
    }

    post ("poster"){
        val table = call.receive<PosterDto>()
        PosterService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("poster"){
        val table = call.receive<PosterDto>()
        PosterService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("poster/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        PosterService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}