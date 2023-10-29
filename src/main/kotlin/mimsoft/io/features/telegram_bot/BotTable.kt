package mimsoft.io.features.telegram_bot

import java.sql.Timestamp

const val TELEGRAM_BOT_TABLE_NAME = "tg_bot"

data class BotTable(
  val id: Long? = null,
  val tgToken: String? = null,
  val tgUsername: String? = null,
  val groupId: String? = null,
  val merchantId: Long? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
