package mimsoft.io.features.order

import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import java.sql.Timestamp

data class OrderDto(
    val id: Long? = null,
    val type: String? = null,
    val status: String? = null,
    val courier : StaffDto? = null,
    val collector : StaffDto? = null,
    val paymentTypeDto: PaymentTypeDto? = null

)