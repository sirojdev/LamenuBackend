package mimsoft.io.lamenu_bot.dtos

import mimsoft.io.lamenu_bot.enums.BotUsersStep
import mimsoft.io.lamenu_bot.enums.Language

object BotUsersMapper {
    fun toBotUsersDto(botUsersTable: BotUsersTable?): BotUsersDto? {
        return if (botUsersTable == null) null
        else BotUsersDto(
            id = botUsersTable.id,
            telegramId  = botUsersTable.telegramId,
            userId  = botUsersTable.userId,
            merchantId = botUsersTable.merchantId,
            step = botUsersTable.step?.let { BotUsersStep.valueOf(it) },
            language = botUsersTable.language?.let { Language.valueOf(it) },
        )
    }
    fun toTable(botUsers: BotUsersDto?): BotUsersTable? {
        return if (botUsers != null) BotUsersTable(
            id = botUsers.id,
            telegramId = botUsers.telegramId,
            userId = botUsers.userId,
            merchantId  = botUsers.merchantId,
            step = botUsers.step?.name,
            language = botUsers.language?.name
        )
        else null
    }
}