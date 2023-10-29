package mimsoft.io.features.badge

import java.sql.Timestamp

const val BADGE_TABLE_NAME = "badge"

data class BadgeTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val nameUz: String? = null,
  val nameRu: String? = null,
  val nameEng: String? = null,
  val textColor: String? = null,
  val bgColor: String? = null,
  val icon: String? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null,
)
