package mimsoft.io.features.app

data class AppDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val googleToken: String? = null,
    val appleToken: String? = null,
    val telegramBotToken: String? = null,
    val selected: String? = null
)