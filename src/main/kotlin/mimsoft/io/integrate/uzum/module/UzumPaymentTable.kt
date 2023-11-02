package mimsoft.io.integrate.uzum.module

import java.sql.Timestamp

const val UZUM_PAYMENT_TABLE = "uzum"

data class UzumPaymentTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val orderId: Long? = null,
  val uzumOrderId: String? = null,
  val createdDate: Timestamp? = null,
  val updatedDate: Timestamp? = null,
  val price: Int? = null,
  val operationType: UzumOperationType? = null
)
