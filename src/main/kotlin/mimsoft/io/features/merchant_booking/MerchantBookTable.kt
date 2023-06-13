package mimsoft.io.features.merchant_booking

import java.sql.Timestamp

const val MERCHANT_BOOK_TABLE_NAME = "merchant_book"
data class MerchantBookTable(
    var id: Long? = null,
    val phone: String? = null,
    val tableId: Long? = null,
    val comment: String? = null,
    val time: Timestamp? = null,
    val merchantId: Long? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null,
)

