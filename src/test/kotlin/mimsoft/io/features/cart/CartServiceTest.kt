package mimsoft.io.features.cart


import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.payment.PaymentDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.staff.StaffDto
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CartServiceTest {

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
            serviceType = "DELIVERY",
            status = "ACCEPTED",
            user = userDto,
            collector = staffDto,
            merchant = merchantDto,
            branch = branchDto,
            comment = "comment",
            productCount = 10,
            totalPrice = 125250
        )
        val response = cartService.check(orderDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun productCount() = testApplication {
        val optionDto = OptionDto(
            id = 34
        )
        val productDto = ProductDto(
            id = 12
        )
        val extraDtoList = listOf(
            ExtraDto(
                id = 45
            )
        )
        val cartItemList = listOf(
            CartItem(
                option = optionDto,
                product = productDto,
                extras = extraDtoList,
                count = 3,
                totalPrice = 2322,
                totalDiscount = 3222
            )
        )
        val response = cartService.productCount(cartItemList)
        assertNotNull(response)
    }
}