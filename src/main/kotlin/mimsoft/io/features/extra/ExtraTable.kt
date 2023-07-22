package mimsoft.io.features.extra

import java.sql.Timestamp

const val EXTRA_TABLE_NAME = "extra"
data class ExtraTable(
    var id: Long? = null,
    val price: Double? = null,
    val image: String? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val productId: Long? = null,
    val nameEng: String? = null,
    var deleted: Boolean? = null,
    val merchantId: Long? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
