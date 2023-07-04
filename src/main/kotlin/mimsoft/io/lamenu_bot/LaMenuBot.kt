package mimsoft.io.lamenu_bot

import mimsoft.io.lamenu_bot.controller.CallBackQueryController
import mimsoft.io.lamenu_bot.controller.MessageController
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

object LaMenuBot : TelegramLongPollingBot() {
    private var callBackQueryController: CallBackQueryController = CallBackQueryController()
    var messageController: MessageController = MessageController()
    override fun getBotToken(): String {
        return "6176478872:AAHG4uN_8gwGYI1P5lBAyQubGXukC9o5Seg"
    }

    override fun getBotUsername(): String {
        return "https://t.me/hdnsjsnsjs_bot"
    }

    override fun onUpdateReceived(update: Update?) {
        //TODO get merchant id
        var merchantId:Long = 1
        if (update!!.hasCallbackQuery()) {
            callBackQueryController.start(update, merchantId);
        } else if (update!!.hasMessage()) {
            messageController.start(update, merchantId)
        }
    }
    fun sendMsg(message: Any): Message? {
        try {
            if (message is SendMessage) {
                return execute(message as SendMessage?)
            } else if (message is EditMessageText) {
                execute(message as EditMessageText?)
            } else if (message is SendPhoto) {
                return execute(message as SendPhoto?)
            } else if (message is SendVideo) {
                return execute(message as SendVideo?)
            } else if (message is SendDocument) {
                return execute(message as SendDocument?)
            } else if (message is SendVoice) {
                return execute(message as SendVoice?)
            } else if (message is DeleteMessage) {
                execute(message as DeleteMessage?)
            } else if (message is SendAudio) {
                execute(message as SendAudio?)
            } else if (message is AnswerInlineQuery) {
                execute(message as AnswerInlineQuery?)
            }else if (message is EditMessageCaption) {
                execute(message as EditMessageCaption?)
            }
        } catch (e: TelegramApiRequestException) {
//            log.error(e.message)
        } catch (e: TelegramApiException) {
//            log.error(e.message)
        }
        return null
    }

}