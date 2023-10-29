package mimsoft.io.waiter.socket

data class WaiterNewOrderDto(
  val visitId: Long? = null,
  val roomId: Long? = null,
  val tableId: Long? = null,
  val status: Boolean? = null
)
