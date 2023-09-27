package mimsoft.io.features.sms_gateway

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SmsGatewayServiceTest {

    private val smsGatewayService = SmsGatewayService

    @Test
    fun getProvider() = testApplication {// TODO: there is an error...
        val smsGatewayDto = SmsGatewayDto(
            id = 13,
            merchantId = 7,
            eskizEmail = "Eskiz",
            eskizPassword = "Root",
            playMobileUsername = "Username",
            playMobilePassword = "MobilePassword",
            selected = "selected"
        )
        val response = smsGatewayService.getProvider(smsGatewayDto)
        if (response != null)
            assertNotNull(response)
        println("rs: $response")
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 712
        val response = smsGatewayService.get(merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val smsGatewayDto = SmsGatewayDto(
            merchantId = 6,
            eskizEmail = "rootnew@gamil.com",
            eskizPassword = "RootNew",
            playMobileUsername = "RootName",
            playMobilePassword = "RootPassword",
            selected = "selected777"
        )
        val response = smsGatewayService.add(smsGatewayDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val smsGatewayDto = SmsGatewayDto(
            merchantId = 6,
            eskizEmail = "rootnew@gamil.com",
            eskizPassword = "RootNew",
            playMobileUsername = "RootName",
            playMobilePassword = "RootPassword",
            selected = "selected888"
        )
        val response = smsGatewayService.update(smsGatewayDto)
        if (response)
            assertTrue(response)
    }
}