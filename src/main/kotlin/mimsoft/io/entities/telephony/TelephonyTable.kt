package mimsoft.io.entities.telephony

import java.sql.Timestamp
const val TELEPHONY_TABLE_NAME = "telephony"
data class TelephonyTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val onlinePbxToken: String? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null
)