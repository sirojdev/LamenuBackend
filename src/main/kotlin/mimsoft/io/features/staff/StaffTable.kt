package mimsoft.io.features.staff

import java.sql.Timestamp

const val STAFF_TABLE_NAME = "staff"

data class StaffTable(
  val id: Long? = null,
  val image: String? = null,
  val phone: String? = null,
  val status: Boolean? = null,
  val gender: String? = null,
  val comment: String? = null,
  val merchantId: Long? = null,
  val password: String? = null,
  val position: String? = null,
  val deleted: Boolean? = null,
  val lastName: String? = null,
  val firstName: String? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val birthDay: Timestamp? = null,
  val branchId: Long? = null
)
