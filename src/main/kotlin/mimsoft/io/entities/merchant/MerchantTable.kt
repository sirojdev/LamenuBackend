package mimsoft.io.entities.merchant

import java.sql.Timestamp

const val RESTAURANT_TABLE_NAME = "merchant"
data class MerchantTable(
    var id: Long? = null,
    var nameUz: String? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var logo: String? = null,
    var domain: String? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    var updated: Timestamp? = null
)
