package mimsoft.io.features.delivery

data class DeliveryDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val yandexClientId: Long? = null,
    val yandexToken: String? = null,
    val expressId: Long? = null,
    val expressToken: String? = null,
    val selected: String? = null
)