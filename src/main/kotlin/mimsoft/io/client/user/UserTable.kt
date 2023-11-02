package mimsoft.io.client.user

import java.sql.Timestamp

const val USER_TABLE_NAME = "users"

data class UserTable(
  val id: Long? = null,
  val badgeId: Long? = null,
  val merchantId: Long? = null,
  val phone: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val image: String? = null,
  val birthDay: Timestamp? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
