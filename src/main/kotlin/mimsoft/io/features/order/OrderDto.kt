package mimsoft.io.features.order

import mimsoft.io.features.order.utils.OrderType
import java.sql.Timestamp

data class OrderDto(
    val id: Long? = null,
    val type: String? = null,
    val status: String? = null,

)