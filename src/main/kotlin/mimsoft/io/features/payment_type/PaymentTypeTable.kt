package mimsoft.io.features.payment_type

import java.sql.Timestamp

const val PAYMENT_TYPE_TABLE_NAME = "payment_type"
data class PaymentTypeTable(
    val id: Long? = null,
    val name: String? = null,
    val icon: String? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)