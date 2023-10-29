package mimsoft.io.features.pantry

import java.sql.Timestamp

const val PANTRY_TABLE_NAME = "pantry"

data class PantryTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val branchId: Long? = null,
  val productId: Long? = null,
  val count: Long? = null,
  val created: Timestamp? = null,
  val deleted: Boolean? = null
)
