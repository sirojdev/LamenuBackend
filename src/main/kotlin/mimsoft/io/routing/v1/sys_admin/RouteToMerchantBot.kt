package mimsoft.io.routing.v1.sys_admin

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.telegram_bot.BotService

fun Route.routeToMerchantBot() {
  val botService = BotService
  get("bots/all") {
    val bots = botService.getAll()
    if (bots.isEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    } else call.respond(bots)
  }
}
