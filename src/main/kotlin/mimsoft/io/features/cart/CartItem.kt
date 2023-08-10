package mimsoft.io.features.cart

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.product.ProductDto

data class CartItem(
    var product: ProductDto? = null,
    val option: OptionDto? = null,
    val extras: List<ExtraDto>? = null,
    var count: Int? = null,
    val totalPrice: Double? = null
)