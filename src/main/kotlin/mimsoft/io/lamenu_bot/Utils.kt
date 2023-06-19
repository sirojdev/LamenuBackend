package mimsoft.io.lamenu_bot

import io.ktor.client.plugins.api.*
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.utils.TextModel
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup

object Utils {
   private var laMenuBot = LaMenuBot
    public fun getText(profile: BotUsersDto, textModel: TextModel?): String? {
        var result: String? = null
        when (profile.language) {
            Language.EN -> result = textModel?.eng
            Language.UZ -> result = textModel?.uz
            Language.RU -> result = textModel?.ru
            else -> {

            }
        }
        return result
    }
    fun sendMsg(chatId:Long,text:String,button:ReplyKeyboardMarkup){
        var sendMsg = SendMessage()
        sendMsg.setChatId(chatId)
        sendMsg.text = text
        sendMsg.replyMarkup = button
        laMenuBot.sendMsg(sendMsg)
    }
    fun sendMsg(chatId:Long,text:String){
        var sendMsg = SendMessage()
        sendMsg.setChatId(chatId)
        sendMsg.text = text
        laMenuBot.sendMsg(sendMsg)
    }
}