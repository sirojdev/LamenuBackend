package mimsoft.io.features.cart

import io.ktor.server.testing.*
import mimsoft.io.features.app.AppDto
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.stoplist.StopListService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CartServiceTest {


    @Test
    fun check() = testApplication {
        val response = StopListService.get(merchantId = 1, id = 35)
        assertNotNull(response)
        assertEquals(true, 11)
    }

    @Test
    fun productCount() {
    }
}