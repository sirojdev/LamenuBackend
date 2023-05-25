package mimsoft.io.entities.category

import java.sql.Timestamp

const val CATEGORY_TABLE_NAME = "category"
data class CategoryTable(
    val id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val image: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)