package mimsoft.io.features.stoplist

import java.sql.Timestamp

const val STOP_LIST_TABLE_NAME = "stoplist"

data class StopListTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val productId: Long? = null,
  val branchId: Long? = null,
  val count: Long? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
