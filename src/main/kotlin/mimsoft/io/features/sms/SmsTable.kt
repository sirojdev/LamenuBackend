package mimsoft.io.features.sms

import java.sql.Timestamp

const val SMS_TABLE = "sms"

data class SmsTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val clientCount: Long? = null,
  val clientId: Long? = null,
  val messageId: Long? = null,
  val time: Timestamp? = null,
  val status: String? = null,
  val context: String? = null,
  val updated: Timestamp? = null,
  val created: Timestamp? = null,
  val deleted: Boolean? = null
)
