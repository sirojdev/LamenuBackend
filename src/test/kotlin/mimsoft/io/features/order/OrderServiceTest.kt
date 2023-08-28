package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.product.ProductDto
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderServiceTest {

    @Test
    fun getAll2() {
    }

    @Test
    fun getAll() = testApplication {
        val search = "CLOSED"
        val limit = 20
        val offset = 5
        val clientId = 21
        val merchantId = 1

        val response = OrderService.getAll(
            mapOf(
                "clientId" to clientId,
                "merchantId" to merchantId,
                "limit" to limit,
                "offset" to offset,
                "search" to search,
            )
        )
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun get() = testApplication {
        val response = OrderService.get(id = 212)
        assert(response.isOk())
        assert(response.body is Order)
    }

    @Test
    fun post() = testApplication {
        val products = mutableListOf<CartItem>()
        val extras = mutableListOf<ExtraDto>()
        extras.add(
            ExtraDto(
                id = 14
            )
        )
        extras.add(
            ExtraDto(
                id = 8
            )
        )
        products.add(
            CartItem(
                product = ProductDto(
                    id = 1
                ),
                count = 4,
                option = OptionDto(
                    id = 3
                ),
                extras = extras
            )
        )
        val order = Order(
            serviceType = "DELIVERY",
            user = UserDto(
                id = 21
            ),
            paymentType = 1,
            branch = BranchDto(
                id = 30
            ),
            address = AddressDto(
                id = 1
            ),
            products = products,
            totalPrice = 24000,
            totalDiscount = 4000,
            merchant = MerchantDto(
                id = 1
            )
        )
        val result = OrderService.post(order = order)
        val result1 = result.httpStatus
        assertEquals(HttpStatusCode.OK, result1)
    }

    @Test
    fun delete() = testApplication {
        val response = OrderService.delete(id = 212)
        val status = response.httpStatus
        assertEquals(HttpStatusCode.OK, status)
    }

    @Test
    fun editPaidOrder() {

    }

    @Test
    fun accepted() {
    }

    @Test
    fun getProductCalculate() {
    }

    @Test
    fun updateOnWave() {
    }

    @Test
    fun updateStatus() {
    }

    @Test
    fun getById() {
    }
}