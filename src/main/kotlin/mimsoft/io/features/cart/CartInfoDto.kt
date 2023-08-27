package mimsoft.io.features.cart

data class CartInfoDto(
    val products: List<CartItem> = emptyList(),
    val serviceType: String? = null,
    val productsPrice: Long? = null,
    val productsDiscount: Long? = null
)
