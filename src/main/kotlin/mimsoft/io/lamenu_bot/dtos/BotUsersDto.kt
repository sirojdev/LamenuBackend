package mimsoft.io.lamenu_bot.dtos

import mimsoft.io.lamenu_bot.enums.BotUsersStep
import mimsoft.io.lamenu_bot.enums.Language

data class BotUsersDto(
    var id: Long? = null,
    var telegramId: Long? = null,
    var userId: Long? = null,
    var merchantId: Long? = null,
    var step: BotUsersStep? = null,
    var language: Language? = null
)