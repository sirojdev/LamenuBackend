package mimsoft.io.lamenu_bot.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.category.repository.CategoryRepositoryImpl.getCategoryByName
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.lamenu_bot.BotTexts
import mimsoft.io.lamenu_bot.LaMenuBot
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.Utils.getDescriptionProduct
import mimsoft.io.lamenu_bot.controller.ButtonController
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.dtos.BotUsersMapper
import mimsoft.io.lamenu_bot.enums.BotUsersStep
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.lamenu_bot.repository.BotUsersRepositoryImpl
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile

object MenuService {
    private var mapper = BotUsersMapper
    private var profileRepository = BotUsersRepositoryImpl
    private var categoryRepository = CategoryRepositoryImpl
    private var buttonController = ButtonController
    private var productRepository = ProductRepositoryImpl
    private var laMenuBot = LaMenuBot
    fun clickMenu(profile: BotUsersDto) {
        GlobalScope.launch {
            //get category by merchantID
            val categoryList: List<CategoryDto?> = categoryRepository.getAll(profile.merchantId)
            // send msg
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.clickMenuText).toString(),
                ButtonController.categoriesButtons(profile, categoryList)
            )
            profile.step = BotUsersStep.CLICK_CATEGORIES
            profileRepository.updateStep(profile)
        }

    }

    fun clickCategories(profile: BotUsersDto, text: String) {
        GlobalScope.launch {
            val category = getCategoryByName(profile.merchantId, profile.language ?: Language.UZ, text)
            if (category == null) {
                Utils.badRequest(profile)
            } else {
                profile.step = BotUsersStep.CLICK_PRODUCT
                profileRepository.updateStep(profile)
                val productList = productRepository.getAllByCategories(profile.merchantId, category?.id)
                Utils.sendMsg(
                    profile.telegramId!!,
                    Utils.getText(profile, BotTexts.clickMenuText).toString(),
                    buttonController.productsButtons(profile, productList)
                )
            }
        }
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
        profile.step = BotUsersStep.EDIT
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.editText).toString(),
            buttonController.settingsButton(profile)
        )
    }

    fun editBack(profile: BotUsersDto) {
        profile.step = BotUsersStep.FREE
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.menuText).toString(),
            buttonController.generalButton(profile)
        )
    }

    fun clickCategoriesBack(profile: BotUsersDto) {
        profile.step = BotUsersStep.FREE
        profileRepository.updateStep(profile)
        Utils.sendMsg(
            profile.telegramId!!,
            Utils.getText(profile, BotTexts.menuText).toString(),
            buttonController.generalButton(profile)
        )
    }

    fun clickProducts(profile: BotUsersDto, text: String) {
        GlobalScope.launch {
            val product: ProductDto? =
                productRepository.getByName(text, profile.language ?: Language.UZ, profile.merchantId ?: 0)
            if (product == null) {
                Utils.badRequest(profile)
            } else {
                // update profile
                profile.step = BotUsersStep.CHOOSE_PRODUCT
                profileRepository.updateStep(profile)
                // send info product
                val sendPhoto = SendPhoto()
                sendPhoto.setChatId(profile.telegramId!!)
                sendPhoto.photo = InputFile(product.image)
                sendPhoto.caption = getDescriptionProduct(product, profile, 1)
                sendPhoto.replyMarkup = buttonController.productCountButton(profile, product.id, 1);
                sendPhoto.parseMode = "MARKDOWN"
                laMenuBot.sendMsg(sendPhoto)
            }
        }

    }


    fun clickProductsBack(profile: BotUsersDto) {
        GlobalScope.launch {
            profile.step = BotUsersStep.CLICK_CATEGORIES
            profileRepository.updateStep(profile)
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.menuText).toString(),
                buttonController.categoriesButtons(profile, categoryRepository.getAll(profile.merchantId))
            )
        }
    }

    fun chooseProductBack(profile: BotUsersDto) {
        GlobalScope.launch {
            profile.step = BotUsersStep.CLICK_PRODUCT
            profileRepository.updateStep(profile)
            Utils.sendMsg(
                profile.telegramId!!,
                Utils.getText(profile, BotTexts.menuText).toString(),
                buttonController.categoriesButtons(profile, categoryRepository.getAll(profile.merchantId))
            )
        }
    }


}