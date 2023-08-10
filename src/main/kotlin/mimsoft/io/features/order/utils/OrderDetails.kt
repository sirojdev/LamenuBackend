package mimsoft.io.features.order.utils

import java.sql.Timestamp

data class OrderDetails(
    val createdAt: Timestamp? = null,
    val deliveryAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val totalPrice: Double? = null,
    val totalDiscount: Double? = null,
    val comment: String? = null,
    val courierId: Long? = null,
    val collectorId: Long? = null,
    val orderId: Long? = null
)
