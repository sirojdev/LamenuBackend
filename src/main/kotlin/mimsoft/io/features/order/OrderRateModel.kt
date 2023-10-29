package mimsoft.io.features.order

data class OrderRateModel(
  val orderId: Long? = null,
  val userId: Long? = null,
  val feedback: String? = null,
  val grade: Int? = null
)
