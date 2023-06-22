package mimsoft.io.features.outcome

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToOutcome(){
    val outcomeMapper = OutcomeMapper;
    get("outcomes"){
        val outcomes = OutcomeService.getAll().map {outcomeMapper.toOutcomeDto(it)}
        if(outcomes.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(outcomes)
    }

    get("outcome/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val outcome = OutcomeService.get(id = id)
        if(outcome == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(outcome)
    }

    post ("outcome"){
        val table = call.receive<OutcomeDto>()
        OutcomeService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("outcome"){
        val table = call.receive<OutcomeDto>()
        OutcomeService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        OutcomeService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}