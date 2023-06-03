package mimsoft.io.features.order.utils

import mimsoft.io.features.product.ProductDto

data class CartItem(
    val product: ProductDto? = null,
    val count: Int? = null
)