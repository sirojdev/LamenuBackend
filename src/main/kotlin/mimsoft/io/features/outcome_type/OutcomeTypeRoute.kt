package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToOutcomeType() {
    get("outcome_types"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val outcomeTypes = OutcomeTypeService.getByMerchantId(merchantId = merchantId)
            if(outcomeTypes.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }else call.respond(outcomeTypes)
    }

    get("outcome_type/{id}"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
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
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val outcomeType = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.add(outcomeType.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put ("outcome_type"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.update(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome_type/{id}"){
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        OutcomeTypeService.delete(merchantId, id)
        call.respond(HttpStatusCode.OK)
    }
}