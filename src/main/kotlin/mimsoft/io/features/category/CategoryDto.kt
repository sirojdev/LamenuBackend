package mimsoft.io.features.category

import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel
data class CategoryDto(
    val id: Long? = null,
    val name: TextModel? = null,
    val image: String? = null,
    val merchantId: Long? = null,
    val groupId: Long? = null,
    val priority: Int? = null,
    var products: List<ProductDto>? = null
)
