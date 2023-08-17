package mimsoft.io.features.order_

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.staff.StaffDto
import java.sql.Timestamp

data class Order(
    val id: Long? = null,
    val serviceType: String? = null,
    val status: String? = null,
    val user: UserDto? = null,
    val collector: StaffDto? = null,
    val merchant: MerchantDto? = null,
    val products: String? = null,
    val paymentType: Long? = null,
    val isPaid: Boolean? = null,
    val comment: String? = null,
    val productCount: Int? = null,
    val details: Map<String, *>? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val deleted: Boolean? = null
)