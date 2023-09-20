package mimsoft.io.features.category

import mimsoft.io.features.category_group.CategoryGroupDto
import mimsoft.io.features.product.ProductInfoDto

data class ClientCategoryDto(
    val categoryDto: CategoryDto? = null,
    val clientProductDto: List<ProductInfoDto>? = null,
    val categoryGroup: CategoryGroupDto? = null
)
