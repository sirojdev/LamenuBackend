package mimsoft.io.entities.menu

import java.sql.Timestamp

const val MENU_TABLE_NAME = "menu"
data class MenuTable(
    var id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
