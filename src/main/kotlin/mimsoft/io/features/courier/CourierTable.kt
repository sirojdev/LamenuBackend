package mimsoft.io.features.courier

import java.sql.Timestamp

const val COURIER_TABLE_NAME = "courier"
data class CourierTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val staffId: Long? = null,
    val balance: Double? = null,
    val lastLocationId: Long? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
