package mimsoft.io.features.income

import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.payment_type.PaymentTypeDto

data class IncomeDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val paidBy: UserDto? = null,
  val paymentType: PaymentTypeDto? = null,
  val amount: Double? = null,
  val time: Timestamp? = null,
  val branchId: Long? = null,
  val incomeType: String? = null
)
