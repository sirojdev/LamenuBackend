package mimsoft.io.entities.telephony

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToTelephony(){
    val telephonyMapper = TelephonyMapper
    get("telephonies"){
        val telephonies = TelephonyService.getAll().map {telephonyMapper.toTelephonyDto(it)}
        if(telephonies.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(telephonies)
    }

    get("telephony/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val telephony = TelephonyService.get(id)
        if(telephony == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(telephony)
    }

    post ("telephony"){
        val table = call.receive<TelephonyDto>()
        TelephonyService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("telephony"){
        val table = call.receive<TelephonyDto>()
        TelephonyService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("telephony/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        TelephonyService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}