package mimsoft.io.features.order.price

data class OrderPriceDto(
    val id: Long? = null,
    val orderId: Long? = null,
    val deliveryPrice: Long? = null,
    val deliveryDiscount: Long? = null,
    val deliveryPromo: Long? = null,
    val productPrice: Long? = null,
    val productDiscount: Long? = null,
    val productPromo: Long? = null,
    val totalPrice: Long? = null,
    val totalDiscount: Long? = null
)
