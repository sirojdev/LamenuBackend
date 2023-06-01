package mimsoft.io.sms

import java.sql.Timestamp

data class SmsTable(
    val id: Long? = null,
    val clientId: Long? = null,
    val messageId: Long? = null,
    val time: Timestamp? = null,
    val status: String? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null,
    val deleted: Boolean? = null
)
