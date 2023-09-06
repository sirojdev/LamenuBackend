package mimsoft.io.features.cart

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.product.ProductDto

data class CartItem(
    var product: ProductDto? = null,
    var option: OptionDto? = null,
    var extras: List<ExtraDto>? = null,
    var count: Int? = null,
    var totalPrice: Long? = null,
    var totalDiscount: Long? = null
)