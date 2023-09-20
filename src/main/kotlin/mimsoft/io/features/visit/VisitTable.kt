package mimsoft.io.features.visit

import java.sql.Timestamp
const val VISIT_TABLE_NAME = "visit"
data class VisitTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val userId: Long? = null,
    val orders: String? = null,
    val waiterId: Long? = null,
    val tableId: Long? = null,
    val time: Timestamp? = null,
    val status: String? = null,
    val paymentTypeId: Long? = null,
    val price: Double? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)