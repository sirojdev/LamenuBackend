package mimsoft.io.features.category

import mimsoft.io.features.category_group.CategoryGroupDto
import mimsoft.io.features.product.ClientProductDto

data class ClientCategoryDto(
    val categoryDto: CategoryDto? = null,
    val clientProductDto: List<ClientProductDto>? = null,
    val categoryGroup: CategoryGroupDto? = null
)
