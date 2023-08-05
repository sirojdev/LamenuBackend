package mimsoft.io.features.order

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import java.sql.Timestamp

data class OrderDto(
    val id: Long? = null,
    val userId: Long? = null,
    val type: String? = null,
    val status: String? = null,
    val courier: StaffDto? = null,
    val user: UserDto? = null,
    val collector: StaffDto? = null,
    val paymentTypeDto: PaymentTypeDto? = null,
    val grade: Double? = null,
    val branch: BranchDto? = null,
    val productCount: Int? = null,
    val totalPrice: Double? = null,
    val created: Timestamp? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,
    val addressName:String?=null,
    val deliveryAt:Timestamp?=null,
    val deliveredAt:Timestamp?=null
)