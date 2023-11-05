package mimsoft.io.courier

import com.google.gson.Gson
import io.ktor.websocket.*
import java.sql.Timestamp
import java.util.*
import mimsoft.io.courier.location.CourierConnection
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType

object CourierSocketService {
  val courierConnections: MutableSet<CourierConnection> =
    Collections.synchronizedSet(LinkedHashSet())

  fun setConnection(courier: CourierConnection): CourierConnection {
    val connection =
      courierConnections.find {
        it.staffId == courier.staffId &&
          it.merchantId == courier.merchantId &&
          it.uuid == courier.uuid
      }
    return if (connection == null) {
      courierConnections.add(courier.copy(connectAt = Timestamp(System.currentTimeMillis())))
      courier
    } else {
      connection
    }
  }

  suspend fun sendLocationToOperators(dto: CourierLocationHistoryDto) {
    val operator =
      OperatorSocketService.operatorConnections.filter { it.merchantId == dto.merchantId }
    for (channel in operator) {
      if (channel.session != null) {
        val model = SocketData(type = SocketType.LOCATION, data = Gson().toJson(dto))
        channel.session?.send(Gson().toJson(model))
      }
    }
  }

  fun courierIdList(merchantId: Long?): ArrayList<Long?> {
    val list = ArrayList<Long?>()
    courierConnections.forEach {
      if (it.merchantId == merchantId) {
        list.add(it.staffId)
      }
    }
    return list
  }
}
