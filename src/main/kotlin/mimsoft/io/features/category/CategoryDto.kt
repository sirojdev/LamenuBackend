package mimsoft.io.features.category

import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel
data class CategoryDto(
    val id: Long? = null,
    val name: TextModel? = null,
    val image: String? = null,
    val merchantId: Long? = null,
    val bgColor: String? = null,
    val textColor: String? = null,
    val groupId: Long? = null,
    var products: List<ProductDto>? = null
)
