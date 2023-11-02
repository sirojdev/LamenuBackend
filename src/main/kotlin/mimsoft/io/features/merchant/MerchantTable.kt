package mimsoft.io.features.merchant

import java.sql.Timestamp

const val MERCHANT_TABLE_NAME = "merchant"

data class MerchantTable(
  var id: Long? = null,
  var nameUz: String? = null,
  var nameRu: String? = null,
  var nameEng: String? = null,
  var sub: String? = null,
  var phone: String? = null,
  var password: String? = null,
  var logo: String? = null,
  val deleted: Boolean? = null,
  val created: Timestamp? = null,
  var updated: Timestamp? = null,
  var isActive: Boolean? = null
)