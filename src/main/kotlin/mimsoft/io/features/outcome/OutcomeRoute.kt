package mimsoft.io.features.outcome

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToOutcome() {
    val outcomeService = OutcomeService
    val outcomeMapper = OutcomeMapper
    get("outcomes") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val outcomes = outcomeService.getAll(merchantId).map { outcomeMapper.toOutcomeDto(it) }
        if (outcomes.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(outcomes)
    }

    get("outcome/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val outcome = outcomeService.get(id = id, merchantId = merchantId)
        if (outcome == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(outcome)
    }

    post("outcome") {
        val outcomeDto = call.receive<OutcomeDto>()
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        outcomeService.add(outcomeDto.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put("outcome") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val outcomeDto = call.receive<OutcomeDto>()
        outcomeService.update(outcomeDto.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        outcomeService.delete(merchantId = merchantId, id = id)
        call.respond(HttpStatusCode.OK)
    }
}