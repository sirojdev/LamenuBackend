package mimsoft.io.features.order

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import java.sql.Timestamp

data class Order(
    val id: Long? = null,
    val posterId: Long? = null,
    val serviceType: String? = null,
    val status: String? = null,
    var user: UserDto? = null,
    val collector: StaffDto? = null,
    var merchant: MerchantDto? = null,
    var branch: BranchDto? = null,
    val products: List<CartItem>? = null,
    val paymentMethod: PaymentTypeDto? = null,
    val isPaid: Boolean? = null,
    val comment: String? = null,
    var productCount: Int? = null,
    var totalPrice: Long? = null,
    val totalDiscount: Long? = null,
    val productPrice: Long? = null,
    val productDiscount: Long? = null,
    var address: AddressDto? = null,
    val courier: StaffDto? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val deleted: Boolean? = null,
    val total: Long? = null,
    var checkoutLink: String? = null
) {
    companion object {
        const val DELIVERY = "DELIVERY"
        const val PICKUP = "PICKUP"
    }
}