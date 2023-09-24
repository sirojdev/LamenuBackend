package mimsoft.io.features.promo

import java.sql.Timestamp

data class PromoDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val amount: Long? = null,
    val name: String? = null,
    val discountType: String? = null,
    val deliveryDiscount: Double? = null,
    val productDiscount: Double? = null,
    val isPublic: Boolean? = null,
    val minAmount: Double? = null,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null
) {


    fun byPercent(): Boolean = discountType == DiscountType.PERCENT.name
}