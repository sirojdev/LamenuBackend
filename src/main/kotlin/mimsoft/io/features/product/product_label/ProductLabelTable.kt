package mimsoft.io.features.product.product_label

import java.sql.Timestamp
const val PRODUCT_LABEL_TABLE = "product_label"
data class ProductLabelTable(
    val id: Long? = null,
    val productId: Long? = null,
    val labelId: Long? = null,
    val merchantId: Long? = null,
    val created: Timestamp? = null,
    val deleted: Boolean? = null
)
