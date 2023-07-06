package mimsoft.io.features.story

import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel

data class StoryDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val image: TextModel? = null,
    val products: List<ProductDto>? = null,
    val priority: Int? = null
)
