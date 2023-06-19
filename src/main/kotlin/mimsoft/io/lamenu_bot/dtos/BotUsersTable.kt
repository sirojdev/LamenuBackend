package mimsoft.io.lamenu_bot.dtos

import mimsoft.io.utils.TextModel
import java.sql.Timestamp

const val BOT_USERS_TABLE_NAME = "bot_users"
data class BotUsersTable (
    var id: Long? = null,
    var telegramId: Long? = null,
    val userId : Long? = null,
    var merchantId: Long? = null,
    var step: String? = null,
    var language: String? = null,
)
