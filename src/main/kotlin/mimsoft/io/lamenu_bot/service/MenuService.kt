package mimsoft.io.lamenu_bot.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.lamenu_bot.BotTexts
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.controller.ButtonController
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.dtos.BotUsersMapper
import mimsoft.io.lamenu_bot.enums.BotUsersStep
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.lamenu_bot.repository.BotUsersRepositoryImpl

object MenuService {
    private var mapper = BotUsersMapper
    private var profileRepository = BotUsersRepositoryImpl
    private var categoryRepository = CategoryRepositoryImpl
    private var buttonController = ButtonController
    fun clickMenu(profile: BotUsersDto) {
        GlobalScope.launch {
            //get category by merchantID
            var categoryList: List<CategoryDto?> = categoryRepository.getAll(profile.merchantId)
            // send msg
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.clickMenuText).toString(),
                ButtonController.clickMenuButtons(profile, categoryList)
            )
            profile.step = BotUsersStep.CLICK_CATEGORIES
            profileRepository.updateStep(profile)
        }

    }

    fun clickCategories(profile: BotUsersDto, text: String) {
        var category = categoryRepository.getCategoryByName(profile, text)
        //TODO get product by categories name


    }

    // statusi free bolsa va u setings ni bossa bu yerga keladi
    fun clickSettings(profile: BotUsersDto) {
        profile.step = BotUsersStep.EDIT
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.editText).toString(),
            ButtonController.settingsButton(profile)
        )
    }

    //bu yerga statusi edit bolsa keladi
    fun editLanguages(profile: BotUsersDto) {
        profile.step = BotUsersStep.EDIT_LANGUAGE
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.editLanguageText).toString(), ButtonController.editLanguageButton(profile)
        )
    }

    fun chooseNewLanguage(profile: BotUsersDto, text: String) {
        GlobalScope.launch {
            when (text) {
                BotTexts.languageButton.uz -> profile.language = Language.UZ
                BotTexts.languageButton.ru -> profile.language = Language.RU
                BotTexts.languageButton.eng -> profile.language = Language.EN
            }
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.goodEditLanguage).toString(),
                buttonController.settingsButton(profile)
            )
            profile.step = BotUsersStep.EDIT
            profileRepository.update(mapper.toTable(profile))
        }
    }

    fun editLanguageBack(profile: BotUsersDto) {
        profile.step=BotUsersStep.EDIT
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.editText).toString(),
            buttonController.settingsButton(profile)
        )
    }

    fun editBack(profile: BotUsersDto) {
        profile.step=BotUsersStep.FREE
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.menuText).toString(),
            buttonController.generalButton(profile)
        )
    }

    fun clickCategoriesBack(profile: BotUsersDto) {
        profile.step=BotUsersStep.FREE
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.menuText).toString(),
            buttonController.generalButton(profile)
        )
    }


}