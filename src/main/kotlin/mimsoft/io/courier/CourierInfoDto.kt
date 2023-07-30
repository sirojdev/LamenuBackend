package mimsoft.io.courier

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import java.sql.Timestamp

data class CourierInfoDto (
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: Timestamp? = null,
    val image: String? = null,
    val gender: String? = null,
    val status: Boolean? = null,
    val balance: Double? = null,
)