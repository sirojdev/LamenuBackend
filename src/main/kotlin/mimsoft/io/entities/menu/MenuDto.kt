package mimsoft.io.entities.menu

import mimsoft.io.entities.category.CategoryDto
import mimsoft.io.entities.product.ProductDto
import mimsoft.io.utils.TextModel

data class MenuDto(
    var categoryList: List<CategoryDto?>,
    var productList: List<ProductDto?>
    )

