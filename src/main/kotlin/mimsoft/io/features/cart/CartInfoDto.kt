package mimsoft.io.features.cart

import mimsoft.io.features.address.AddressDto

data class CartInfoDto(
    val products: List<CartItem> = emptyList(),
    val address : AddressDto? = null,
    val productCount: Int? = null,
    val productsPrice: Long? = null,
    val productsDiscount: Long? = null,
    val totalPrice: Long? = null,
    val totalDiscount: Long? = null
)
