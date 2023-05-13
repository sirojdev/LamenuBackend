package mimsoft.io.entities.product

import java.sql.Timestamp

data class ProductTable(
    var id: Long? = null,
    var menuId: Long? = null,
    var nameUz: String? = null,
    var nameRu: String? = null,
    var nameEng: String? = null,
    var descriptionUz: String? = null,
    var descriptionRu: String? = null,
    var descriptionEng: String? = null,
    var image: String? = null,
    var price: Double? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
