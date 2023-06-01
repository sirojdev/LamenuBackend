package mimsoft.io.entities.delivery

import java.sql.Timestamp
const val DELIVERY_TABLE_NAME = "delivery"
data class DeliveryTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val yandexClientId: Long? = null,
    val yandexToken: String? = null,
    val expressId: Long? = null,
    val expressToken: String? = null,
    val selected: String? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null
)