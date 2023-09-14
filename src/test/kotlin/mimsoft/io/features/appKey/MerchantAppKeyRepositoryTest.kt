package mimsoft.io.features.appKey

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MerchantAppKeyRepositoryTest {

    val merchantAppKeyRepository = MerchantAppKeyRepository

    @Test
    fun add() = testApplication {
        val merchantAppKeyDto = MerchantAppKeyDto(
            merchantId = 2,
            appKey = 2
        )
        val response = merchantAppKeyRepository.add(merchantAppKeyDto)
        println("response = $response")
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 2
        val response = merchantAppKeyRepository.getAll(merchantId)
        println("response: $response")
        assertNotNull(response)
        assert(response[0].appKey != null)
    }

    @Test
    fun getByAppId() = testApplication {
        val app_id: Long = 1
        val response = merchantAppKeyRepository.getByAppId(app_id)
        assertNotNull(response)
    }

    @Test
    fun deleteByAppId() = testApplication {
        val id: Long = 2
        val response = merchantAppKeyRepository.deleteByAppId(id)
        assertTrue(response)
    }
}