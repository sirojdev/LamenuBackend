package mimsoft.io.integrate.yandex.module

import java.sql.Timestamp

const val YANDEX_ORDER = "yandex"

data class YandexOrderDto(
    val id: Long? = null,
    val claimId: String? = null,
    val orderId: Long? = null,
    val orderStatus: String? = null,
    val version: Int? = null,
    val operationId: String? = null,
    val createdDate: Timestamp? = null,
    val updatedDate: Timestamp? = null,
    val yandexKey: String? = null
)