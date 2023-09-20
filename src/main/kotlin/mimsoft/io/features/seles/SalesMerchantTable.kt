package mimsoft.io.features.seles

import java.sql.Timestamp

const val SALES_MERCHANT_TABLE_NAME = "sales_merchant"
data class SalesMerchantTable(
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
