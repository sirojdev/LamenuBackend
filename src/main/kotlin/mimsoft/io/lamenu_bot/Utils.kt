package mimsoft.io.lamenu_bot

import mimsoft.io.features.product.ProductDto
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.utils.TextModel
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup

object Utils {
    private var laMenuBot = LaMenuBot
    fun getText(profile: BotUsersDto, textModel: TextModel?): String? {
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

    fun sendMsg(chatId: Long, text: String, button: ReplyKeyboardMarkup) {
        var sendMsg = SendMessage()
        sendMsg.setChatId(chatId)
        sendMsg.text = text
        sendMsg.replyMarkup = button
        laMenuBot.sendMsg(sendMsg)
    }

    fun editMsg(chatId: Long, messageId: Int, text: String, button: InlineKeyboardMarkup) {
        var editMsg = EditMessageText()
        editMsg.setChatId(chatId)
        editMsg.text = text
        editMsg.messageId=messageId
        editMsg.replyMarkup = button
        laMenuBot.sendMsg(editMsg)
    }
    fun editMessageCaption(chatId: Long, messageId: Int, text: String, button: InlineKeyboardMarkup) {
        var editMsg = EditMessageCaption()
        editMsg.setChatId(chatId)
        editMsg.caption = text
        editMsg.messageId=messageId
        editMsg.replyMarkup = button
        laMenuBot.sendMsg(editMsg)
    }

    fun sendMsg(chatId: Long, text: String) {
        var sendMsg = SendMessage()
        sendMsg.setChatId(chatId)
        sendMsg.text = text
        laMenuBot.sendMsg(sendMsg)
    }

    fun badRequest(profile: BotUsersDto?) {
        sendMsg(profile?.telegramId!!, getText(profile!!, BotTexts.badRequest).toString())
    }

    fun getDescriptionProduct(product: ProductDto, profile: BotUsersDto, count: Int): String? {
        var description = "*" + getText(profile, product.name) + "*" + "\n"
        description += getText(profile, product.description)
        description += "\n"
        description = description + "*" + getText(profile, BotTexts.cost) + "*" + " : "
        description += (product.costPrice!! * count)
        return description

    }
}