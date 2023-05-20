package mimsoft.io.entities.product

import java.sql.Timestamp

const val PRODUCT_TABLE_NAME = "product"
data class ProductTable(
    var id: Long? = null,
    var menuId: Long? = null,
    var nameUz: String? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var descriptionUz: String? = null,
    var descriptionRu: String? = null,
    var descriptionEn: String? = null,
    var image: String? = null,
    var costPrice: Double? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
