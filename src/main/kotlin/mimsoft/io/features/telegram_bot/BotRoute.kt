package mimsoft.io.features.telegram_bot

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Route.routeToBot() {
    val botService: BotRepository = BotService
    val botMapper = BotMapper
    get("bots") {
        val bots = botService.getAll().map {
            BotMapper.toBotDto(it)
        }
        if (bots.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(bots)
    }

    get("bot/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val bot = BotMapper.toBotDto(botService.get(id))
        if(bot == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(bot)
    }


    post("bot") {
        val table = call.receive<BotDto>()
        botService.add(BotMapper.toBotTable(table))
        call.respond(HttpStatusCode.OK)
    }

    put("bot") {
        val table = call.receive<BotDto>()
        botService.update(BotMapper.toBotTable(table))
        call.respond(HttpStatusCode.OK)
    }

    delete("bot/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        botService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}