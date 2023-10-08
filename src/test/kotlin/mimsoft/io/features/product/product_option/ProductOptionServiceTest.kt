package mimsoft.io.features.product.product_option

import io.ktor.server.testing.*
import kotlin.test.Test


class ProductOptionServiceTest { // TODO:  sql table not created...


    private val productOptionService = ProductOptionService

    @Test
    fun add() = testApplication {
        val productOptionDto = ProductOptionDto(
            productId = null,
            optionId = null,
            merchantId = null
        )
        val response = productOptionService.add(productOptionDto)

    }

    @Test
    fun getOptionsByProductId() {
    }

    @Test
    fun deleteProductOption() {
    }
}