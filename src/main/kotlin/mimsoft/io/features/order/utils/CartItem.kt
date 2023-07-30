package mimsoft.io.features.order.utils

import mimsoft.io.features.product.ProductDto

data class CartItem(
    var product: ProductDto? = null,
    var count: Int? = null
)