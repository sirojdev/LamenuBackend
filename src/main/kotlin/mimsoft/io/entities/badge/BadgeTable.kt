package mimsoft.io.entities.badge

import mimsoft.io.utils.TextModel
import java.sql.Timestamp

const val BADGE_TABLE_NAME = "badge"
data class BadgeTable(
    val id: Long? = null,
    val menuId: Long? = null,
    val name: TextModel? = null,
    val textColor: String? = null,
    val bgColor: String? = null,
    val icon: String? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null,
)
