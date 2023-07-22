package mimsoft.io.features.courier.checkout

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.order.utils.OrderWrapper
import java.sql.Timestamp

data class CourierTransactionDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val courier: CourierDto? = null,
    val time: Timestamp? = null,
    val amount: Double? = null,
    val order: OrderWrapper? = null,
    val branch: BranchDto? = null
)
