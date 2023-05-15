package mimsoft.io.entities.order

import java.sql.Timestamp

data class OrderTable(
    val id: Long? = null,
    val userId: Long? = null,
    val userPhone: String? = null,
    val type: Int? = null,
    val products: String? = null,
    val status: String? = null,
    val addLat: Double? = null,
    val addLong: Double? = null,
    val addDesc: String? = null,
    val createdAt: Timestamp? = null,
    val deliveryAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val deleted: Boolean? = null
)
