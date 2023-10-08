package mimsoft.io.features.delivery

import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeliveryServiceTest {

    private val deliveryService = DeliveryService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = deliveryService.get(merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val deliveryDto = DeliveryDto(
            merchantId = 9,
            yandexClientId = 3,
            expressId = 4,
            yandexToken = "yandextoken",
            expressToken = "exoresstoken",
            selected = "Yandex"
        )
        val response = deliveryService.add(deliveryDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val deliveryDto = DeliveryDto(
            id = 22,
            merchantId = 9,
            yandexClientId = 3,
            expressId = 4,
            yandexToken = "sdfssd",
            expressToken = "sdsdfsd",
            selected = "Yandex"
        )
        val response = deliveryService.update(deliveryDto)
        if (response)
            assertTrue(response)
    }
}