package mimsoft.io.features.order

import mimsoft.io.features.order.utils.OrderType
import java.sql.Timestamp

data class OrderDto(
    val id: Long? = null,
    val type: OrderType? = null,
    val status: String? = null,
    val createdAt: Timestamp? = null,
    val deliveryAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val totalPrice: Double? = null,
    val totalDiscount: Double? = null,
    val comment: String? = null,
)