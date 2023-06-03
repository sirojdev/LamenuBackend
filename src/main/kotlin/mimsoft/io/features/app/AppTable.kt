package mimsoft.io.features.app

import java.sql.Timestamp
const val APP_TABLE_NAME = "app"
data class AppTable(
    val id: Long?=null,
    val merchantId: Long? = null,
    val googleToken: String? = null,
    val appleToken: String? = null,
    val telegramBotToken: String? = null,
    val selected: String? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null
)