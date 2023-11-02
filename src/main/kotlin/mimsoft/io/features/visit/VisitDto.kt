package mimsoft.io.features.visit

import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.visit.enums.CheckStatus

data class VisitDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val user: UserDto? = null,
  val orders: List<CartVisitDto>? = null,
  val waiter: StaffDto? = null,
  val table: TableDto? = null,
  val time: Timestamp? = null,
  val status: CheckStatus? = null,
  val isActive: Boolean? = null,
  val payment: PaymentTypeDto? = null,
  val price: Double? = null,
  val clientCount: Int? = null,
  val branch: BranchDto? = null
)
