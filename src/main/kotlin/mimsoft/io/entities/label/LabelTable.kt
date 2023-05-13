package mimsoft.io.entities.label

import java.sql.Timestamp
import java.time.LocalDateTime

data class LabelTable(
    var id: Long? = null,
    val menuId: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val textColor: String? = null,
    val bgColor: String? = null,
    val icon: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
