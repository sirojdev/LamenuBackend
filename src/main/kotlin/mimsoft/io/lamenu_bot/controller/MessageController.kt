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
                    start(profile,merchantId,telegramId)
                }
                when (profile?.step) {
                    BotUsersStep.CHOOSE_LANG -> chooseLanguage(profile, text)
                    BotUsersStep.FREE -> generalMenu(profile, text)
                    BotUsersStep.CLICK_CATEGORIES -> clickCategories(profile, text)
                    BotUsersStep.EDIT -> editSettings(profile, text)
                    BotUsersStep.EDIT_LANGUAGE -> editLanguage(profile, text)
                    else -> {}
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

    private fun start(profile: BotUsersDto?, merchantId: Long, telegramId: Long) {
        GlobalScope.launch {
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
        }
    }

    private fun clickCategories(profile: BotUsersDto, text: String) {
        when (text) {
            BotTexts.back.ru, BotTexts.back.eng, BotTexts.back.uz -> menuServices.clickCategoriesBack(profile)
            else -> menuServices.clickCategories(profile, text)
        }
    }

    private fun chooseLanguage(profile: BotUsersDto, text: String) {
        GlobalScope.launch {
            when (text) {
                BotTexts.languageButton.uz -> profile.language = Language.UZ
                BotTexts.languageButton.ru -> profile.language = Language.RU
                BotTexts.languageButton.eng -> profile.language = Language.EN
            }
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.menuText).toString(),
                buttonController.generalButton(profile)
            )
            profile.step = BotUsersStep.FREE
            botUsersRepository.update(mapper.toTable(profile))
        }
    }

    private fun editLanguage(profile: BotUsersDto, text: String) {
        when (text) {
            BotTexts.languageButton.ru, BotTexts.languageButton.uz, BotTexts.languageButton.eng -> menuServices.chooseNewLanguage(
                profile,
                text
            )

            BotTexts.back.ru, BotTexts.back.uz, BotTexts.back.eng -> menuServices.editLanguageBack(profile)
        }
    }

    private fun generalMenu(profile: BotUsersDto, text: String) {
        when (text) {
            BotTexts.history.ru, BotTexts.history.eng, BotTexts.history.uz -> {
                //TODO history
            }

            BotTexts.settings.ru, BotTexts.settings.uz, BotTexts.settings.eng -> menuServices.clickSettings(profile)
            BotTexts.menu.ru, BotTexts.menu.uz, BotTexts.menu.eng -> menuServices.clickMenu(profile)
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

    private fun editSettings(profile: BotUsersDto, text: String) {
        when (text) {
            BotTexts.editLanguage.ru, BotTexts.editLanguage.eng, BotTexts.editLanguage.uz
            -> menuServices.editLanguages(profile)

            BotTexts.back.ru, BotTexts.back.eng, BotTexts.back.uz
            -> menuServices.editBack(profile)
        }
    }
}

