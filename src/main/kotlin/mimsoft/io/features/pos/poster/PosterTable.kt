package mimsoft.io.features.pos.poster

import java.sql.Timestamp

const val POSTER_TABLE = "poster"

data class PosterTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val jowiApiKey: String? = null,
  val joinPosterApiKey: String? = null,
  val rKeeperClientId: Long? = null,
  val rKeeperClientSecret: String? = null,
  val selected: String? = null,
  val deleted: Boolean? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null
)
