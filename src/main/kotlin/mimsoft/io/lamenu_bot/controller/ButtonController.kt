package mimsoft.io.lamenu_bot.controller

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.lamenu_bot.BotTexts
import mimsoft.io.lamenu_bot.Utils
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import kotlin.collections.ArrayList


object ButtonController {
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


    fun generalButton(profile: BotUsersDto): ReplyKeyboardMarkup {
        val menu = button(Utils.getText(profile, BotTexts.menu))
        val history = button(Utils.getText(profile, BotTexts.history))
        val settings = button(Utils.getText(profile, BotTexts.settings))
        val row1 = buttonRow(menu)
        val row2 = buttonRow(history, settings)
        val rowList = buttonRowList(row1, row2)
        return keyboard(rowList)
    }

    fun clickMenuButtons(profile: BotUsersDto, categoryList: List<CategoryDto?>): ReplyKeyboardMarkup {
        var rowList = ArrayList<KeyboardRow>()
        for (x in 0..categoryList.size step 2) {
            var row = KeyboardRow()
            val button1 = button(Utils.getText(profile, categoryList[x]?.name))
            row = if (x < categoryList.size-1) {
                val button2 = button(Utils.getText(profile, categoryList[x + 1]?.name))
                buttonRow(button1, button2)
            } else {
                buttonRow(button1)
            }
            rowList.add((row))
        }
        return keyboard(rowList)
    }
}