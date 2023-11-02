package mimsoft.io.flat

import java.sql.Timestamp

const val FLAT_TABLE_NAME = "flat"

data class FlatTable(
  val id: Long? = null,
  val name: String? = null,
  val branchId: Long? = null,
  val restaurantId: Long? = null,
  val deleted: Boolean? = null,
  var created: Timestamp? = null,
  var updated: Timestamp? = null
)
