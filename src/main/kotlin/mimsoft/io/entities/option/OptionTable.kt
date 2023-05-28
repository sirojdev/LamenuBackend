package mimsoft.io.entities.option

import java.sql.Timestamp

const val OPTION_TABLE_NAME = "options"

data class OptionTable(
    var id: Long? = null,
    val parentId: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val descriptionUz: String? = null,
    val descriptionRu: String? = null,
    val descriptionEn: String? = null,
    val image: String? = null,
    val price: Double? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
