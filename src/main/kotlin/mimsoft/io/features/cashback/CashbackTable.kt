package mimsoft.io.features.cashback

import java.sql.Timestamp
const val CASHBACK_TABLE_NAME = "cashback"
data class CashbackTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val minCost: Double? = null,
    val maxCost: Double? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
