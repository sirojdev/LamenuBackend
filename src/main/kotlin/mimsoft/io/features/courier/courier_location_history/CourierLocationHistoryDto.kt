package mimsoft.io.features.courier.courier_location_history

import java.sql.Timestamp

data class CourierLocationHistoryDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val staffId: Long? = null,
    val time: Timestamp? = null,
    val name: String? = null
)