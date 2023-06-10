package mimsoft.io.features.staff

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto

data class StaffDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val phone: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: String? = null,
    val image: String? = null,
    val position: String? = null,
    val token: String? = null,
    val comment: String? = null,
    var lastLocation: CourierLocationHistoryDto? = null
)