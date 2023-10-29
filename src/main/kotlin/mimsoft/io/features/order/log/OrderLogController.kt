import com.google.firebase.FirebaseException
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.order.log.OrderLogModel

object OrderLogController {

  suspend fun getAll(orderId: Long?): List<OrderLogModel?> {
    return emptyList()
  }

  suspend fun add(
    log: OrderLogModel?,
    orderId: Long?,
    user: UserDto? = null,
    courier: CourierDto? = null,
    type: Int?,
    isDone: Boolean? = null,
    delivery: Boolean? = true
  ): Boolean {
    return true
  }

  fun <T> sender(send: T): Boolean {
    try {
      send
    } catch (e: FirebaseException) {
      e.printStackTrace()
    }
    return true
  }
}
