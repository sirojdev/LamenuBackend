package mimsoft.io.features.courier.checkout

import java.sql.Timestamp
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.order.Order

data class CourierTransactionDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val orderId: Long? = null,
  val courier: CourierDto? = null,
  val time: Timestamp? = null,
  val amount: Double? = null,
  val order: Order? = null,
  val branch: BranchDto? = null,
)
