package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.outcomeTypeRoute() {
    val outcomeTypeMapper = OutcomeTypeMapper;
    get("outcome_types"){
        val outcomeTypes = OutcomeTypeService.getAll().map {outcomeTypeMapper.toOutcomeTypeDto(it)}
        if(outcomeTypes.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(outcomeTypes)
    }

    get("outcome_type/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val outcomeType = OutcomeTypeService.get(id)
        if(outcomeType == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(outcomeType)
    }

    post ("outcome_type"){
        val table = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("outcome_type"){
        val table = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome_type/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        OutcomeTypeService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}