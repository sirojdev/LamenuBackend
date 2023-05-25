package mimsoft.io.entities.order.utils

data class OrderType (
    val id: Long? = null,
    val name: OrderTypeEnums? = null
)
enum class OrderTypeEnums {
    DELIVERY,
    TAKEAWAY
}