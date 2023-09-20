package mimsoft.io.entities.product

import mimsoft.io.utils.TextModel

data class ProductDto(
    var id: Long? = null,
    var menuId: Long? = null,
    var name: TextModel? = null,
    var description: TextModel? = null,
    var image: String? = null,
    var costPrice: Double? = null,
)
