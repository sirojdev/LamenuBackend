package mimsoft.io.features.order.log

import java.sql.Timestamp
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.utils.TextModel

const val TO_COURIER = 2
const val TO_CLIENT = 1
const val TO_ALL = 3

data class OrderLogModel(
  val id: Long? = null,
  val orderId: Long? = null,
  val type: String? = null,
  val courier: CourierDto? = null,
  val title: TextModel? = null,
  val body: TextModel? = null,
  val from: String? = null,
  val to: String? = null,
  val sendTo: Int? = null,
  val createdAt: Timestamp? = null,
)
