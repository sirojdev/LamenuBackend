package mimsoft.io.lamenu_bot.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.repository.BotUsersRepositoryImpl
import org.telegram.telegrambots.meta.api.objects.Update

class CallBackQueryController {
    private var buttonController = ButtonController
    private var botUsersRepository = BotUsersRepositoryImpl
    private var productRepository = ProductRepositoryImpl
    fun start(update: Update, merchantId: Long) {
        GlobalScope.launch {
            val callback = update.callbackQuery
            val data = callback.data
            val telegramID = callback.from.id
            val messageId = callback.message.messageId
            val profile = botUsersRepository.getByTelegramId(telegramID, merchantId)
            if (data.startsWith("minus/p_id/")) {
                if (getProductCount(data) > 1) {
                    var product = productRepository.mapper.toProductDto(
                        productRepository.get(getProductId(data), merchantId)
                    )
                    Utils.editMessageCaption(
                        telegramID,
                        messageId,
                        Utils.getDescriptionProduct(
                            product!!,
                            profile!!, getProductCount(data).toInt() - 1
                        ).toString(),
                        buttonController.productCountButton(profile!!, getProductId(data), getProductCount(data) - 1)
                    )
                }
            } else if (data.startsWith("plus/p_id/")) {
                var product = productRepository.mapper.toProductDto(
                    productRepository.get(getProductId(data), merchantId)
                )
                Utils.editMessageCaption(
                    telegramID,
                    messageId,
                    Utils.getDescriptionProduct(
                        product!!,
                        profile!!, getProductCount(data).toInt() - 1
                    ).toString(),
                    buttonController.productCountButton(profile!!, getProductId(data), getProductCount(data) + 1)
                )
            } else if (data.startsWith("basket/p_id/")) {

            }
        }
    }

    private fun getProductId(data: String): Long {
        var array = data.split("/")
        return array[3].toLong()
    }

    private fun getProfileId(data: String): Long {
        var array = data.split("/")
        return array[2].toLong()
    }

    private fun getProductCount(data: String): Long {
        var array = data.split("/")
        return array[4].toLong()
    }

}