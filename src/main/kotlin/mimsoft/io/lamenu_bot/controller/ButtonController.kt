package mimsoft.io.lamenu_bot.controller

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.lamenu_bot.BotTexts
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*


object ButtonController {
    private fun inlineButton(text: String, callbackData: String): InlineKeyboardButton {
        val button = InlineKeyboardButton()
        button.text = text
        button.callbackData = callbackData
        return button
    }

    private fun inlineButtonRow(vararg inlineKeyboardButtons: InlineKeyboardButton): List<InlineKeyboardButton> {
        val row: MutableList<InlineKeyboardButton> = LinkedList()
        row.addAll(listOf(*inlineKeyboardButtons))
        return row
    }


    private fun inlineButtonRowList(vararg rows: List<InlineKeyboardButton>): List<List<InlineKeyboardButton>> {
        val collection: MutableList<List<InlineKeyboardButton>> = LinkedList()
        collection.addAll(listOf(*rows))
        return collection
    }


    private fun inlineKeyboard(collection: List<List<InlineKeyboardButton>>): InlineKeyboardMarkup {
        val keyboardMarkup = InlineKeyboardMarkup()
        keyboardMarkup.keyboard = collection
        return keyboardMarkup
    }

    private fun button(text: String?): KeyboardButton {
        val button = KeyboardButton()
        button.text = text!!
        return button
    }

    private fun buttonRow(vararg keyboardButtons: KeyboardButton): KeyboardRow {
        val row = KeyboardRow()
        row.addAll(listOf(*keyboardButtons))
        return row
    }


    private fun buttonRowList(vararg rows: KeyboardRow): ArrayList<KeyboardRow> {
        val collection: ArrayList<KeyboardRow> = ArrayList()
        collection.addAll(listOf(*rows))
        return collection
    }


    private fun keyboard(rowList: MutableList<KeyboardRow>): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.keyboard = rowList
        replyKeyboardMarkup.resizeKeyboard = true //buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.selective = true // bottinga strelka qoshadi;
        replyKeyboardMarkup.oneTimeKeyboard = true
        return replyKeyboardMarkup
    }

    fun phoneKeyboard(profile: BotUsersDto): ReplyKeyboardMarkup {
        val button = KeyboardButton()
        button.text = Utils.getText(profile, BotTexts.sendContact).toString()
        button.requestContact = true
        val row = buttonRow(button)
        val rowList = buttonRowList(row)
        return keyboard(rowList)
    }

    fun languageButton(): ReplyKeyboardMarkup {
        val rus = button(BotTexts.languageButton.ru)
        val eng = button(BotTexts.languageButton.eng)
        val uzb = button(BotTexts.languageButton.uz)
        val row1 = buttonRow(uzb, rus, eng)
        val list = buttonRowList(row1)
        return keyboard(list)
    }

    fun editLanguageButton(profile: BotUsersDto): ReplyKeyboardMarkup {
        var back = button(Utils.getText(profile, BotTexts.back))
        val rus = button(BotTexts.languageButton.ru)
        val eng = button(BotTexts.languageButton.eng)
        val uzb = button(BotTexts.languageButton.uz)
        val list = buttonRowList(buttonRow(back, uzb), buttonRow(rus, eng))
        return keyboard(list)
    }


    fun generalButton(profile: BotUsersDto): ReplyKeyboardMarkup {
        val menu = button(Utils.getText(profile, BotTexts.menu))
        val history = button(Utils.getText(profile, BotTexts.history))
        val settings = button(Utils.getText(profile, BotTexts.settings))
        val row1 = buttonRow(menu)
        val row2 = buttonRow(history, settings)
        val rowList = buttonRowList(row1, row2)
        return keyboard(rowList)
    }

    fun categoriesButtons(profile: BotUsersDto, categoryList: List<CategoryDto?>): ReplyKeyboardMarkup {
        var rowList = ArrayList<KeyboardRow>()
        var backButton = button(Utils.getText(profile, BotTexts.back))
        rowList.add((buttonRow(backButton)))
        for (x in 0..categoryList.size - 1 step 2) {
            var row = KeyboardRow()
            val button1 = button(Utils.getText(profile, categoryList[x]?.name))
            row = if (x < categoryList.size - 1) {
                val button2 = button(Utils.getText(profile, categoryList[x + 1]?.name))
                buttonRow(button1, button2)
            } else {
                buttonRow(button1)
            }
            rowList.add(row)
        }
        return keyboard(rowList)
    }

    fun productsButtons(profile: BotUsersDto, productList: List<ProductDto?>): ReplyKeyboardMarkup {
        val rowList = ArrayList<KeyboardRow>()
        val backButton = button(Utils.getText(profile, BotTexts.back))
        rowList.add((buttonRow(backButton)))
        for (x in 0..productList.size - 1 step 2) {
            var row = KeyboardRow()
            val button1 = button(Utils.getText(profile, productList[x]?.name))
            row = if (x < productList.size - 1) {
                val button2 = button(Utils.getText(profile, productList[x + 1]?.name))
                buttonRow(button1, button2)
            } else {
                buttonRow(button1)
            }
            rowList.add(row)
        }
        return keyboard(rowList)
    }

    fun settingsButton(profile: BotUsersDto): ReplyKeyboardMarkup {
        var editLanguage = button(Utils.getText(profile, BotTexts.editLanguage))
        var back = button(Utils.getText(profile, BotTexts.back))
        return keyboard(buttonRowList(buttonRow(editLanguage, back)))
    }

    fun productCountButton(profile: BotUsersDto, productID: Long?, count: Long?): InlineKeyboardMarkup {
        return inlineKeyboard(
            inlineButtonRowList(
                inlineButtonRow(
                    inlineButton("-", "minus/p_id/" + profile.id + "/" + productID+"/"+count),
                    inlineButton("" + count, "count/p_id/" + profile.id + "/" + productID+"/"+count),
                    inlineButton("+", "plus/p_id/" + profile.id + "/" + productID+"/"+count),
                ),
                inlineButtonRow(
                    inlineButton(
                        Utils.getText(profile, BotTexts.addToBasket).toString(),
                        "basket/p_id/" + profile.id + "/" + productID+"/"+count
                    )
                )
            )
        )

    }
}