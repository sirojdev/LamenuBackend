package mimsoft.io.features.story_info

import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel

data class StoryInfoDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val image: TextModel? = null,
    val priority: Int? = null,
    val products: List<ProductDto>? = null,
    val buttonBgColor: String? = null,
    val buttonTextColor: String? = null,
    val buttonText: TextModel? = null,
    val storyId: Long? = null
)
