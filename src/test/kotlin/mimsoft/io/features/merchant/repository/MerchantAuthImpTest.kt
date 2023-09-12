package mimsoft.io.features.merchant.repository

import io.ktor.server.testing.*
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MerchantAuthImpTest {

    val merchantAuthImp = MerchantAuthImp

    @Test
    fun auth() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng",
        )
        val merchantDto = MerchantDto(
            sub = "Acer",
            logo = "Acer logo",
            name = textModel,
            phone = "+998900070707",
            password = "70707",
            token = "@KLJJkjjosjdflkdlkksjffios"
        )
        val response = merchantAuthImp.auth(merchantDto)
        assertNotNull(response)
    }

    @Test
    fun logout() = testApplication {
        val uuid = "47d8a2b9-9538-4355-8cc8-a2c6587a07b8+2023-05-20 16:36:09.249"
        val response = merchantAuthImp.logout(uuid)
        assertTrue(response)
    }

    @Test
    fun getByPhonePassword() = testApplication {
        val phone = "+998900070707"
        val password = "70707"
        val response = merchantAuthImp.getByPhonePassword(phone, password)
        assertNotNull(response)
    }
}