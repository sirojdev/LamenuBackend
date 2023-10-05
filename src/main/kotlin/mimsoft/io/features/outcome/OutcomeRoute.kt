package mimsoft.io.features.outcome

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal
import kotlin.math.min

fun Route.routeToOutcome() {
    val outcomeService = OutcomeService
    get("outcomes") {
        val pr = call.principal<BasePrincipal>()
        val search = call.parameters["search"]
        val filter = call.parameters["filter"]
        val staffId = call.parameters["staffId"]?.toLongOrNull()
        val limit = min(call.parameters["limit"]?.toIntOrNull() ?: 10, 50)
        val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
        val merchantId = pr?.merchantId
        val outcomes = outcomeService.getAll(merchantId = merchantId, limit = limit, offset = offset, search = search, filter = filter, staffId = staffId)
        call.respond(outcomes)
    }

    get("outcome/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<BasePrincipal>()
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
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        outcomeService.add(outcomeDto.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    put("outcome") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val outcomeDto = call.receive<OutcomeDto>()
        outcomeService.update(outcomeDto.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("outcome/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        outcomeService.delete(merchantId = merchantId, id = id)
        call.respond(HttpStatusCode.OK)
    }
}