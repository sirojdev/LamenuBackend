package mimsoft.io.features.promo

import java.sql.Timestamp

data class PromoDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val discountType: DiscountType? = null,
    val deliveryDiscount: Double? = null,
    val productDiscount: Double? = null,
    val isPublic: Boolean? = null,
    val minAmount: Double? = null,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null
)
