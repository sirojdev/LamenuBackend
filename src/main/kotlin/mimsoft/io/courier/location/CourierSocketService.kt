package mimsoft.io.courier.location

import mimsoft.io.features.courier.CourierService
import java.sql.Timestamp
import java.util.*

object CourierSocketService {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    suspend fun connect(connection: Connection): Boolean {
        val courier = CourierService.getByStaffId(staffId = connection.staffId, merchantId = connection.merchantId)
            ?: return false
        connections += connection.copy(
            connectAt = Timestamp(System.currentTimeMillis())
        )
        return true
    }
}