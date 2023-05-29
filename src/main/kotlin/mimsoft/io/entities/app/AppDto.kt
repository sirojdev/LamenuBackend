package mimsoft.io.entities.app

data class AppDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val googleToken: String? = null,
    val appleToken: String? = null,
    val telegramBotToken: String? = null
)