package mimsoft.io.features.product

import mimsoft.io.utils.TextModel

data class ProductDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val description: TextModel? = null,
    val image: String? = null,
    val costPrice: Long? = null,
    val active: Boolean? = null,
)
