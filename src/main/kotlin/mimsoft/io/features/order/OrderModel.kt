package mimsoft.io.features.order

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.payment_type.PaymentTypeDto

data class OrderModel(
    val id: Long? = null,
    val products: List<CartItem>? = null,
    val address: AddressDto? = null,
    val branch: BranchDto? = null,
    val promo: String? = null,
    val cashbackAmount: Double? = null,
    val paymentType: PaymentTypeDto? = null,
    val time: String? = null,
    val comment: String? = null,
    val user: UserDto? = null,
    val orderType: String? = null
)
