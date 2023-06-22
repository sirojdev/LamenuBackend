package mimsoft.io.features.staff

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.order.OrderDto

data class StaffDto(
    val id: Long? = null,
    val token: String? = null,
    val image: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val status: Boolean? = null,
    val comment: String? = null,
    val birthDay: String? = null,
    val password: String? = null,
    val position: String? = null,
    val merchantId: Long? = null,
    val lastName: String? = null,
    val firstName: String? = null,
    val allOrderCount: Long? = null,
    val todayOrderCount: Long? = null,
    val orders: List<OrderDto?>? = null,
    val activeOrderCount: Long? = null,
    var lastLocation: CourierLocationHistoryDto? = null
)