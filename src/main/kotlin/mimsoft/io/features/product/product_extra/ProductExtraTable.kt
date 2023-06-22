package mimsoft.io.features.product.product_extra

import java.sql.Timestamp
const val PRODUCT_EXTRA_TABLE = "product_extra"
data class ProductExtraTable(
    val id: Long? = null,
    val productId: Long? = null,
    val extraId: Long? = null,
    val merchantId: Long? = null,
    val created: Timestamp? = null,
    val deleted: Boolean? = null
)
