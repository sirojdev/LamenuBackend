package mimsoft.io.entities.seles

import com.sun.org.apache.xpath.internal.operations.Bool
import java.sql.Timestamp

const val RESTAURANT_TABLE_NAME = "merchant"
data class MerchantTable(
    var id: Long? = null,
    var nameUz: String? = null,
    var nameRu: String? = null,
    var nameEng: String? = null,
    var subdomain: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var logo: String? = null,
    var domain: String? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    var updated: Timestamp? = null,
    var isActive: Boolean? = null
)
