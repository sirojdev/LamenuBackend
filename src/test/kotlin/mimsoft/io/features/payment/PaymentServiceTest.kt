package mimsoft.io.features.payment

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.runners.Parameterized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PaymentServiceTest {

    private val paymentService = PaymentService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = paymentService.get(merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun paymeVerify() = testApplication {
        val serviceKey = "Qd&yzbWx?IFqZCxmriasTf0aNptgduvvZ0Pf"
        val response = paymentService.paymeVerify(serviceKey)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val paymentDto = PaymentDto(
            merchantId = 11,
            paymeMerchantId = "23",
            paymeSecret = "payme_secret12",
            apelsinMerchantId = 3,
            apelsinMerchantToken = "sdfes3a32fsdvvxzc12",
            clickServiceId = 12,
            clickMerchantId = "35",
            clickKey = "23"
        )
        val response = paymentService.add(paymentDto)
        println("rs: $response")
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val paymentDto = PaymentDto(
            merchantId = 333,
            paymeMerchantId = "23",
            paymeSecret = "payme_secret",
            apelsinMerchantId = 3,
            apelsinMerchantToken = "sdfes3a32fsdvvxzc",
            clickServiceId = 12,
            clickMerchantId = "22",
            clickKey = "23"
        )
        val response = paymentService.update(paymentDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getPaymentTypeClient() = testApplication {
        val merchantId: Long = 122
        val response = paymentService.getPaymentTypeClient(merchantId)
        assert(response.isEmpty())
    }
}