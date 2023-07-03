package mimsoft.io.features.product

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.utils.TextModel

data class ProductDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val description: TextModel? = null,
    val image: String? = null,
    val costPrice: Long? = null,
    val active: Boolean? = null,
    val categoryId: Long? = null,
    val productIntegration: ProductIntegrationDto? = null,
    val category: CategoryDto? = null,
    val timeCookingMin: Long? = null,
    val timeCookingMax: Long? = null,
    val deliveryEnabled: Boolean? = null,
    val count: Long? = -1
)