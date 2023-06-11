package mimsoft.io.features.courier.courier_location_history
import java.sql.Timestamp
const val COURIER_LOCATION_HISTORY_SERVICE = "courier_location_history"
class CourierLocationHistoryTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val staffId: Long? = null,
    val time: Timestamp? = null
)