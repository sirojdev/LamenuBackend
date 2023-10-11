package mimsoft.io.features.courier

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto

data class CourierDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val branchId: Long? = null,
    val staffId: Long? = null,
    val balance: Double? = null,
    val lastLocation: CourierLocationHistoryDto? = null,
    val type: String? = null
)
