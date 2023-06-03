package mimsoft.io.features.order.utils

data class OrderType (
    val id: Long? = null,
    val name: OrderTypeEnums? = null
)
enum class OrderTypeEnums {
    DELIVERY,
    TAKEAWAY
}