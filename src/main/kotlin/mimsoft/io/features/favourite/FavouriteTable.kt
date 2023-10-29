package mimsoft.io.features.favourite

import java.sql.Timestamp

const val FAVOURITE_TABLE_NAME = "favourite"

data class FavouriteTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val productId: Long? = null,
  val clientId: Long? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
