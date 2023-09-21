package mimsoft.io.features.product.product_label

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ProductLabelServiceTest {

    private val productLabelService = ProductLabelService

    @Test
    fun add() = testApplication {
        val productLabelDto = ProductLabelDto(
            productId = 788,
            labelId = 34,
            merchantId = 2
        )
        val response = productLabelService.add(productLabelDto)
        assertEquals(HttpStatusCode.OK, response)
    }

    @Test
    fun getLabelsByProductId() = testApplication {
        val productId: Long = 788
        val merchantId: Long = 22
        val response = productLabelService.getLabelsByProductId(productId, merchantId)
        println("rs: $response")
        assert(response.isEmpty())
    }

    @Test
    fun deleteProductLabel() = testApplication {
        val productLabelDto = ProductLabelDto(
            productId = 788,
            labelId = 15,
            merchantId = 2
        )
        val response = productLabelService.deleteProductLabel(productLabelDto)
        println("rs: $response")
    }
}