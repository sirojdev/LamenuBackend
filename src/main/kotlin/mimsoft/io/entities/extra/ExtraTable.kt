package mimsoft.io.entities.extra

import java.sql.Timestamp

const val EXTRA_TABLE_NAME = "extra"
data class ExtraTable(
    var id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val price: Double? = null,
    val descriptionUz: String? = null,
    val descriptionRu: String? = null,
    val descriptionEn: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
