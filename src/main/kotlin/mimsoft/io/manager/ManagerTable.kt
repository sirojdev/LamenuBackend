package mimsoft.io.manager

import java.sql.Timestamp

data class ManagerTable(
  val id: Long? = null,
  val username: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val phone: String? = null,
  val password: String? = null,
  val token: String? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
