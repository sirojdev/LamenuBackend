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
import mimsoft.io.lamenu_bot.repository.BotUsersRepositoryImpl

object MenuService {
    private var mapper = BotUsersMapper
    private var profileRepository = BotUsersRepositoryImpl
    private var categoryRepository = CategoryRepositoryImpl
     fun clickMenu(profile: BotUsersDto) {
         GlobalScope.launch {
             //get category by merchantID
             var categoryList: List<CategoryDto?> = categoryRepository.getAll(profile.merchantId)
             // send msg
             Utils.sendMsg(
                 profile.telegramId!!,
                 Utils.getText(profile, BotTexts.clickMenuText).toString(),
                 ButtonController.clickMenuButtons(profile,categoryList)
             )
             //TODO update step for back
             profile.step = BotUsersStep.CLICK_MENU
             profileRepository.updateStep(profile)

         }

    }

}