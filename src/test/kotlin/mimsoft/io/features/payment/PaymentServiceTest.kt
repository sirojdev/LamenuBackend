package mimsoft.io.features.payment

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PaymentServiceTest {

    val paymentService = PaymentService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = paymentService.get(merchantId)
        assertNotNull(response)
    }

    @Test
    fun paymeVerify() = testApplication {
        val serviceKey = ""
        val response = paymentService.paymeVerify(serviceKey)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val paymentDto = PaymentDto(
            merchantId = 1,
            paymeMerchantId = "23",
            paymeSecret = "payme_secret",
            apelsinMerchantId = 3,
            apelsinMerchantToken = "sdfes3a32fsdvvxzc",
            clickServiceId = 12,
            clickMerchantId = "35",
            clickKey = "23"
        )
        val response = paymentService.add(paymentDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val paymentDto = PaymentDto(
            id = 2,
            merchantId = 1,
            paymeMerchantId = "23",
            paymeSecret = "payme_secret",
            apelsinMerchantId = 3,
            apelsinMerchantToken = "sdfes3a32fsdvvxzc",
            clickServiceId = 12,
            clickMerchantId = "35",
            clickKey = "23"
        )
        val response = paymentService.update(paymentDto)
        assertTrue(response)
    }

    @Test
    fun getPaymentTypeClient() = testApplication {
        val merchantId: Long = 1
        val response = paymentService.getPaymentTypeClient(merchantId)
        assertNotNull(response)
    }
}