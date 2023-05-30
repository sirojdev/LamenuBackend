package mimsoft.io.entities.app

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Route.routeToApp(){
    val appMapper = AppMapper
    get("apps"){
        val apps = AppService.getAll().map {appMapper.toAppDto(it)}
        if(apps.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(apps)
    }

    get("app/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val app = AppService.get(id)
        if(app == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(app)
    }

    post ("app"){
        val table = call.receive<AppDto>()
        AppService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("app"){
        val table = call.receive<AppDto>()
        AppService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("app/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        AppService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}