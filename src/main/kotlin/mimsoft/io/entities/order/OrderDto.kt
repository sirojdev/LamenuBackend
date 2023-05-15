package mimsoft.io.entities.order

import java.sql.Timestamp

data class OrderDto(
    val id: Long? = null,
    val type: Int? = null,
    val status: String? = null,
    val createdAt: Timestamp? = null,
    val deliveryAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val totalPrice: Double? = null,
    val totalDiscount: Double? = null
)