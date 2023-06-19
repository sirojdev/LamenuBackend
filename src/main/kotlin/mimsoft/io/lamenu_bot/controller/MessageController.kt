package mimsoft.io.lamenu_bot.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mimsoft.io.lamenu_bot.BotTexts
import mimsoft.io.lamenu_bot.dtos.BotUsersTable
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.dtos.BotUsersMapper
import mimsoft.io.lamenu_bot.enums.BotUsersStep
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.lamenu_bot.repository.BotUsersRepositoryImpl
import mimsoft.io.lamenu_bot.service.MenuService
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

class MessageController {
    private var botUsersRepository = BotUsersRepositoryImpl
    private val mapper = BotUsersMapper
    private val buttonController = ButtonController
    private val menuServices = MenuService
    fun start(update: Update, merchantId: Long) {
        GlobalScope.launch {
            val message: Message = update.message
            val telegramId: Long = message.chatId
            val profile = botUsersRepository.getByTelegramId(telegramId, merchantId)
            if (message.hasText()) {
                val text: String = message.text
                if (text == "/start") {
                    if (profile == null) {
                        createNewUsers(merchantId, telegramId)
                        Utils.sendMsg(telegramId, "\uD83C\uDF10 Tilni tanlang:", ButtonController.languageButton())
                    } else {
                        //update profile
                        profile.step = BotUsersStep.FREE
                        botUsersRepository.update(mapper.toTable(profile))
                        //send msg
                        Utils.sendMsg(
                            telegramId,
                            Utils.getText(profile, BotTexts.menuText).toString(),
                            ButtonController.generalButton(profile)
                        )
                    }
                } else if (profile?.step == BotUsersStep.CHOOSE_LANG) {
                    when (text) {
                        BotTexts.languageButton.uz -> profile.language = Language.UZ
                        BotTexts.languageButton.ru -> profile.language = Language.RU
                        BotTexts.languageButton.eng -> profile.language = Language.EN
                    }
                    Utils.sendMsg(
                        telegramId,
                        Utils.getText(profile, BotTexts.menuText).toString(),
                        buttonController.generalButton(profile)
                    )
                    profile.step = BotUsersStep.FREE
                    botUsersRepository.update(mapper.toTable(profile))
                } else if (profile?.step == BotUsersStep.FREE) {
                    generalMenu(profile, text)
                } else if (profile?.step == BotUsersStep.CLICK_MENU){

                }
            } else if (message.hasContact()) {
//                if (profile?.step == BotUsersStep.SEND_CONTACT) {
//                    profile.step = BotUsersStep.FREE
//                    botUsersRepository.update(mapper.toTable(profile))
//                    //TODO get user by phone and update
//                }
            }

        }
    }

    private fun generalMenu(profile: BotUsersDto, text: String) {
        when (profile.language) {
            Language.RU -> {
                when (text) {
                    BotTexts.history.ru -> {
                        //TODO history
                    }

                    BotTexts.settings.ru -> {
                        //TODO settings
                    }

                    BotTexts.menu.ru -> {
                        menuServices.clickMenu(profile)
                    }
                }
            }

            Language.UZ -> {
                when (text) {
                    BotTexts.history.uz -> {

                    }

                    BotTexts.settings.uz -> {

                    }

                    BotTexts.menu.uz -> {
                        menuServices.clickMenu(profile)
                    }
                }

            }

            Language.EN -> {
                when (text) {
                    BotTexts.history.eng -> {

                    }

                    BotTexts.settings.eng -> {

                    }

                    BotTexts.menu.eng -> {
                        menuServices.clickMenu(profile)
                    }
                }

            }

            else -> {

            }
        }

    }


    private suspend fun createNewUsers(merchantId: Long, telegramId: Long) {
        botUsersRepository.add(
            BotUsersTable(
                telegramId = telegramId,
                step = BotUsersStep.CHOOSE_LANG.name,
                merchantId = merchantId,
            )
        )
    }

}