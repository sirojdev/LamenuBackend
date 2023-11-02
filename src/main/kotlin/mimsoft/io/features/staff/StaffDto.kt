package mimsoft.io.features.staff

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.order.Order

data class StaffDto(
  val id: Long? = null,
  val token: String? = null,
  val branchId: Long? = null,
  val image: String? = null,
  val phone: String? = null,
  val gender: String? = null,
  val balance: Long? = null,
  val isActive: Boolean? = null,
  val status: Boolean? = null,
  val comment: String? = null,
  var birthDay: String? = null,
  val password: String? = null,
  val newPassword: String? = null,
  val position: StaffPosition? = null,
  val merchantId: Long? = null,
  val lastName: String? = null,
  val firstName: String? = null,
  val allOrderCount: Long? = null,
  val todayOrderCount: Long? = null,
  val orders: List<Order?>? = null,
  val activeOrderCount: Long? = null,
  var lastLocation: CourierLocationHistoryDto? = null
)
