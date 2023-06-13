package mimsoft.io.features.merchant_booking

import java.sql.Timestamp

data class MerchantBookDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val phone: String? = null,
    val tableId: Long? = null,
    val time: Timestamp? = null,
    val comment: String? = null
)
