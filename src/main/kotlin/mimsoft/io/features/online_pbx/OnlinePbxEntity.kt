package mimsoft.io.features.online_pbx

import java.sql.Timestamp

data class OnlinePbxEntity(
  val id: Long? = null,
  val merchantId: Long? = null,
  val domain: String? = null,
  val phone: String? = null,
  val key: String? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
