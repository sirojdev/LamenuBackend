package mimsoft.io.features.favourite

import mimsoft.io.features.product.ProductDto

data class FavouriteDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val clientId: Long? = null,
    val product: ProductDto? = null
)
