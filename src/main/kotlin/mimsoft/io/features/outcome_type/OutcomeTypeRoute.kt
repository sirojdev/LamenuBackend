@file:Suppress("NAME_SHADOWING")

package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.outcomeTypeRoute() {
    val merchantId = 1L
    get("outcome_types"){
        val outcomeTypes = OutcomeTypeService.getByMerchantId(merchantId = merchantId)
        if(outcomeTypes.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(outcomeTypes)
    }

    get("outcome_type/{id}"){
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val outcomeType = OutcomeTypeService.get(merchantId, id)
        if(outcomeType == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(outcomeType)
    }

    post ("outcome_type"){
        val merchantId = 1L
        val outcomeTypee = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.add(outcomeTypee.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put ("outcome_type"){
        val merchantId = 1L
        val table = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.update(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome_type/{id}"){
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        OutcomeTypeService.delete(merchantId, id)
        call.respond(HttpStatusCode.OK)
    }
}