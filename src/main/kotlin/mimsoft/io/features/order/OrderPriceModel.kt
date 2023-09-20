package mimsoft.io.features.order

data class OrderPriceModel(
    val totalPrice: Long? = null,
    val totalDiscount: Long? = null,
    val totalPriceWithDiscount: Long? = null
)
