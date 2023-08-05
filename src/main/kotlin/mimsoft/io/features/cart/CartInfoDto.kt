package mimsoft.io.features.cart

import mimsoft.io.features.address.AddressDto

data class CartInfoDto(
    val products: List<CartItem> = emptyList(),
    val address : AddressDto? = null,
    val productCount: Int? = null,
    val productsPrice: Double? = null,
    val productsDiscount: Double? = null,
    val totalPrice: Double? = null,
    val totalDiscount: Double? = null
)
