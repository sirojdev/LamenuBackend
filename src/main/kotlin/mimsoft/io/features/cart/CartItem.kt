package mimsoft.io.features.cart

import mimsoft.io.features.product.ProductDto

data class CartItem(
    var product: ProductDto? = null,
    var count: Int? = null
)