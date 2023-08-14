package mimsoft.io.features.order

import java.sql.Timestamp

const val ORDER_TABLE_NAME = "orders"
data class OrderTable(
    val id: Long? = null,
    val userId: Long? = null,
    val userPhone: String? = null,
    val serviceType: String? = null,
    val products: String? = null,
    val status: String? = null,
    val addLat: Double? = null,
    val addLong: Double? = null,
    val addDesc: String? = null,
    val paymentType: Long? = null,
    val isPaid: Boolean? = null,
    val createdAt: Timestamp? = null,
    val deliveryAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val deleted: Boolean? = null,
    val comment: String? = null,
    val merchantId: Long? = null,
    val courierId: Long? = null,
    val collectorId: Long? = null,
    val productCount: Int? = null,
    val grade: Double? = null
)
