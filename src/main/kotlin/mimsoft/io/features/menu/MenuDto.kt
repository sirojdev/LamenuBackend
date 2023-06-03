package mimsoft.io.features.menu

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel

data class MenuDto(
    var categoryList: List<CategoryDto?>,
    var productList: List<ProductDto?>
    )

