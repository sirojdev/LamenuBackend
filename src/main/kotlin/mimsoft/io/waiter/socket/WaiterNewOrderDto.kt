package mimsoft.io.waiter.socket

data class WaiterNewOrderDto(
    val clientFirstName: String? = null,
    val clientLastName: String? = null,
    val roomId: Long? = null,
    val tableId: Long? = null
)