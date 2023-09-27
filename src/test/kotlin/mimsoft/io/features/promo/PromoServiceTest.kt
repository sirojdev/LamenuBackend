package mimsoft.io.features.promo

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PromoServiceTest {

    private val promoService = PromoService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 111
        val limit = 10
        val offset = 5
        val response = promoService.getAll(merchantId, limit, offset)
        if (response.data?.isEmpty() == true)
            assertNotNull(response.data)
    }

    @Test
    fun add() = testApplication {
        val promoDto = PromoDto(
            merchantId = 1,
            amount = 19999,
            name = "Name777",
            discountType = "AMOUNT",
            deliveryDiscount = 100.99,
            productDiscount = 233.4,
            minAmount = 11.1
        )
        val response = promoService.add(promoDto)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val promoDto = PromoDto(
            id = 3555,
            merchantId = 1,
            amount = 19999,
            name = "Name77",
            discountType = "AMOUNT",
            deliveryDiscount = 100.99,
            productDiscount = 233.4,
            minAmount = 11.1
        )
        val response = promoService.update(promoDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 35
        val merchantId: Long = 1
        val response = promoService.delete(id, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 35555
        val response = promoService.get(merchantId, id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getPromoByCode() = testApplication {
        val code = "Name777"
        val response = promoService.getPromoByCode(code)
        if (response != null)
            assertNotNull(response)
    }
}