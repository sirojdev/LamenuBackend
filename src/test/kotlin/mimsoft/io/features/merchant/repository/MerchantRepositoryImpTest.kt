package mimsoft.io.features.merchant.repository

import io.ktor.server.testing.*
import mimsoft.io.features.merchant.MerchantTable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MerchantRepositoryImpTest {

    val merchantRepositoryImp = MerchantRepositoryImp

    @Test
    fun getInfo() = testApplication {
        val sub = "Mimsoft"
        val response = merchantRepositoryImp.getInfo(sub)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val response = merchantRepositoryImp.getAll()
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 8
        val response = merchantRepositoryImp.get(id)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val merchantTable = MerchantTable(
            nameUz = "StringUz",
            nameRu = "StringRu",
            nameEng = "StringEng",
            sub = "Samsung",
            phone = "+998991010101",
            password = "1010101",
            logo = "Samsung logo"
        )
        val response = merchantRepositoryImp.add(merchantTable)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val merchantTable = MerchantTable(
            nameUz = "StringUz",
            nameRu = "StringRu",
            nameEng = "StringEng",
            sub = "Samsung",
            phone = "+998991010101",
            password = "1010101",
            logo = "Samsung logo"
        )
        val response = merchantRepositoryImp.update(merchantTable)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 7
        val response = merchantRepositoryImp.delete(id)
        assertTrue(response)
    }

    @Test
    fun getMerchantById() = testApplication {
        val merchantId: Long = 1
        val response = merchantRepositoryImp.getMerchantById(merchantId)
        assertNotNull(response)
    }
}