package mimsoft.io.features.cart


import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.payment.PaymentDto
import mimsoft.io.features.staff.StaffDto
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CartServiceTest {// TODO: kamchilik bor...

    val cartService = CartService

    @Test
    fun check() = testApplication {
        val merchantId: Long = 1
        val userDto = UserDto(
            id = 24
        )
        val staffDto = StaffDto(
            id = 9
        )
        val merchantDto = MerchantDto(
            id = 1
        )
        val branchDto = BranchDto(
            id = 31
        )
        val cartItem = CartItem(

        )
        val paymentDto = PaymentDto(
            id = 1
        )

        val orderDto = Order(
            id = 2,
            posterId = null,
            serviceType = "DELIVERY",
            status = "ACCEPTED",
            user = userDto,
            collector = staffDto,
            merchant = merchantDto,
            branch = branchDto,
            products = null,
            paymentMethod = null,
            isPaid = false,
            comment = "comment",
            productCount = 10,
            totalPrice = 125250,
            totalDiscount = null,
            productPrice = null,
            productDiscount = null,
            address = null,
            courier = null,
            createdAt = null,
            updatedAt = null,
            deleted = null,
            total = null
        )
        val response = cartService.check(orderDto, merchantId)
        println(response)

    }

    @Test
    fun productCount() {
    }
}