package mimsoft.io.features.product.product_option

import java.sql.Timestamp

const val PRODUCT_OPTION_TABLE = "product_option"

data class ProductOptionTable(
  val id: Long? = null,
  val productId: Long? = null,
  val optionId: Long? = null,
  val merchantId: Long? = null,
  val created: Timestamp? = null,
  val deleted: Boolean? = null
)
