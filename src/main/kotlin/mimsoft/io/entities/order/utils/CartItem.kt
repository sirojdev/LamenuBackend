package mimsoft.io.entities.order.utils

import mimsoft.io.entities.product.ProductDto

data class CartItem(
    val product: ProductDto? = null,
    val count: Int? = null
)