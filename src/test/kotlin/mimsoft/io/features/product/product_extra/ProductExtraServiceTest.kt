package mimsoft.io.features.product.product_extra

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductExtraServiceTest {// TODO: ENG: Not connected to the database; UZ: Ma'lumotlar bazasi bilan bog'lanmagan;

    private val productExtraService = ProductExtraService

    @Test
    fun add() = testApplication {
        val productExtraDto = ProductExtraDto(
            productId = 3,
            extraId = 4,
            merchantId = 1
        )
        val response = productExtraService.add(productExtraDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getExtrasByProductId() = testApplication {
        val productId: Long = 1
        val merchantId: Long = 5
        val response = productExtraService.getExtrasByProductId(productId, merchantId)
        assertNotNull(response)
        @Test
        fun deleteProductExtra() = testApplication {
            val productExtraDto = ProductExtraDto(
                productId = 34,
                extraId = 21,
                merchantId = 1
            )
            productExtraService.deleteProductExtra(productExtraDto)
        }
    }
}