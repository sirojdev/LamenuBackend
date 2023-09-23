package mimsoft.io.features.merchant.repository

import io.ktor.server.testing.*
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MerchantAuthImpTest {

    private val merchantAuthImp = MerchantAuthImp

    @Test
    fun auth() = testApplication {
        val textModel = TextModel(
            uz = "Test",
            ru = "Test",
            eng = "Test",
        )
        val merchantDto = MerchantDto(
            sub = "Test",
            logo = "Test",
            name = textModel,
            phone = "+990901001015",
            password = "Root"
        )
        val response = merchantAuthImp.auth(merchantDto)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun logout() = testApplication {
        val uuid = "12baf97f22-bad8-4aa7-90c8-d9730eacba91+2023-06-07 00:01:07.335"
        val response = merchantAuthImp.logout(uuid)
        println("rs: $response")
        assertTrue(response)
    }

    @Test
    fun getByPhonePassword() = testApplication {
        val phone = "+990901001015"
        val password = "Root"
        val response = merchantAuthImp.getByPhonePassword(phone, password)
        println("rs: $response")
        assertNotNull(response)
    }
}