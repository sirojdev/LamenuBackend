@file:Suppress("NAME_SHADOWING")

package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.outcomeTypeRoute() {
    get("outcome_types"){
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val outcomeTypes = OutcomeTypeService.getByMerchantId(merchantId = merchantId)
        if(outcomeTypes.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(outcomeTypes)
    }

    get("outcome_type/{id}"){
        val pr = call.principal<MerchantPrincipal>()
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
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val outcomeTypee = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.add(outcomeTypee.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put ("outcome_type"){
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<OutcomeTypeDto>()
        OutcomeTypeService.update(table.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome_type/{id}"){
        val pr = call.principal<MerchantPrincipal>()
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