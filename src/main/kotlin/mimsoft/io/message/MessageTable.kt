package mimsoft.io.message

import mimsoft.io.sms.SmsTable
import java.sql.Timestamp

data class MessageTable(
    val id: Long? = null,
    val content: String? = null,
    val time: Timestamp? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null,
    val deleted: Boolean? = null
)
