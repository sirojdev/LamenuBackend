package mimsoft.io.client.menu

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.ProductDto

data class MenuDto(
    var categoryList: List<CategoryDto?>,
    var productList: List<ProductDto?>
)

