package mimsoft.io.entities.category

import java.sql.Timestamp
import java.time.LocalDateTime

data class CategoryTable(
    val id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val image: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)