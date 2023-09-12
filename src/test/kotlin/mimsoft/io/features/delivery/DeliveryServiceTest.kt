package mimsoft.io.features.delivery

import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeliveryServiceTest {

    val deliveryService = DeliveryService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = deliveryService.get(merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val deliveryDto = DeliveryDto(
            merchantId = 5,
            yandexClientId = 3,
            expressId = 4,
            yandexToken = "sodjosidksdnsieros",
            expressToken = "3j3j4l3j43kjskjdfjso",
            selected = "Yandex"
        )
        val response = deliveryService.add(deliveryDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val deliveryDto = DeliveryDto(
            id = 14,
            merchantId = 5,
            yandexClientId = 3,
            expressId = 4,
            yandexToken = "sdfssd",
            expressToken = "sdsdfsd",
            selected = "Yandex"
        )
        val response = deliveryService.update(deliveryDto)
        assertTrue(response)
    }
}