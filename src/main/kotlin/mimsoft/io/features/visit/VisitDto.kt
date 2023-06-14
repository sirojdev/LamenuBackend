package mimsoft.io.features.visit

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.log.OrderLog
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class VisitDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val user: UserDto? = null,
    val orders: List<OrderLog>? = null,
    val waiter: StaffDto? = null,
    val table: TableDto? = null,
    val time: Timestamp? = null,
    val status: CheckStatus? = null,
    val payment: PaymentTypeDto? = null,
    val price: Double? = null
)