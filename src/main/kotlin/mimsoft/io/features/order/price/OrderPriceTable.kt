package mimsoft.io.features.order.price

import java.sql.Timestamp

data class OrderPriceTable (
    val id: Long? = null,
    val orderId: Long? = null,
    val deliveryPrice: Double? = null,
    val deliveryDiscount: Double? = null,
    val deliveryPromo: Double? = null,
    val productPrice: Double? = null,
    val productDiscount: Double? = null,
    val productPromo: Double? = null,
    val totalPrice: Double? = null,
    val totalDiscount: Double? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null,
)