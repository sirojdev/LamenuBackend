package mimsoft.io.courier.location

import io.ktor.server.websocket.*
import mimsoft.io.features.courier.CourierService
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

object CourierSocketService {
    val locationConnection: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())
    val courierNewOrderConnection: MutableSet<Connection> = Collections.synchronizedSet(LinkedHashSet())

    fun courierIdList(
        merchantId: Long?
    ): ArrayList<Long?> {
        val list = ArrayList<Long?>()
        courierNewOrderConnection.forEach{
            if (it.merchantId==merchantId){
                list.add(it.staffId)
            }
        }
        return list
    }


}