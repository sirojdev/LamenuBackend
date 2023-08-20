package mimsoft.io.features.telegram_bot

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToBot() {
    val botService: BotRepository = BotService
    val botMapper = BotMapper
    get("bots") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val bots = botService.getAll(merchantId=merchantId).map {
            botMapper.toBotDto(it)
        }
        if (bots.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(bots)
    }

    get("bot/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val bot = botMapper.toBotDto(botService.get(id=id, merchantId=merchantId))
        if(bot == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(bot)
    }

    post("bot") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<BotDto>()
        botService.add(botMapper.toBotTable(table.copy(merchantId=merchantId)))
        call.respond(HttpStatusCode.OK)
    }

    put("bot") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val table = call.receive<BotDto>()
        botService.update((table.copy(merchantId=merchantId)))
        call.respond(HttpStatusCode.OK)
    }

    delete("bot/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        botService.delete(id=id, merchantId=merchantId)
        call.respond(HttpStatusCode.OK)
    }
}