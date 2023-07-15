package mimsoft.io.features.promo

import java.sql.Timestamp
const val PROMO_TABLE_NAME = "promo"
data class PromoTable(
    val id: Long? = null,
    val name: String? = null,
    val merchantId: Long? = null,
    val discountType: String? = null,
    val deliveryDiscount: Double? = null,
    val productDiscount: Double? = null,
    val isPublic: Boolean? = null,
    val amount: Long? = null,
    val minAmount: Double? = null,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
