package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import mimsoft.io.lamenu_bot.LaMenuBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun Application.runBot(){
    var botsApi: TelegramBotsApi? = null
    try {
        botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
    try {
        if (botsApi != null) {
            botsApi.registerBot(LaMenuBot)
        }
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}