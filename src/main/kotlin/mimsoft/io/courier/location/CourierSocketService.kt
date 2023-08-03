package mimsoft.io.courier.location

import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import java.sql.Timestamp
import java.util.*

object CourierSocketService {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    val adminConnections = Collections.synchronizedSet<AdminConnection?>(LinkedHashSet())


    suspend fun connect(connection: Connection): Boolean {
        val courier = CourierService.getByStaffId(staffId = connection.staffId, merchantId = connection.merchantId)
            ?: return false
        connections += connection.copy(
            connectAt = Timestamp(System.currentTimeMillis())
        )
        return true
    }

    suspend fun adminConnect(admin: AdminConnection): Boolean {
        val courier = MerchantRepositoryImp.get(admin.merchantId)
            ?: return false
        adminConnections += admin.copy(
            connectAt = Timestamp(System.currentTimeMillis())
        )
        return true
    }
}