package mimsoft.io.features.product

import java.sql.Timestamp

const val PRODUCT_TABLE_NAME = "product"
data class ProductTable(
    val id: Long? = null,
    val menuId: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val descriptionUz: String? = null,
    val descriptionRu: String? = null,
    val descriptionEng: String? = null,
    val image: String? = null,
    val active: Boolean? = null,
    val costPrice: Long? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null
)
