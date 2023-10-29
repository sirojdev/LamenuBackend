package mimsoft.io.features.courier.checkout

import java.sql.Timestamp

const val COURIER_TRANSACTION_TABLE = "courier_transaction"

data class CourierTransactionTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val courierId: Long? = null,
  val time: Timestamp? = null,
  val amount: Double? = null,
  val fromOrderId: Long? = null,
  val branchId: Long? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
