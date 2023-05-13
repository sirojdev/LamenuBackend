package mimsoft.io.entities.option

import java.sql.Timestamp
import java.time.LocalDateTime

data class OptionTable(
    var id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val descriptionUz: String? = null,
    val descriptionRu: String? = null,
    val descriptionEng: String? = null,
    val image: String? = null,
    val price: Double? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
