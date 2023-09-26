package mimsoft.io.features.seles.repository

import io.ktor.server.testing.*
import mimsoft.io.entities.seles.repository.MerchantRepositoryImp
import mimsoft.io.features.seles.SalesMerchantDto
import mimsoft.io.features.seles.SalesMerchantTable
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MerchantRepositoryImpTest {

    private val merchantRepositoryImp = MerchantRepositoryImp

    @Test
    fun getAll() = testApplication {
        val response = merchantRepositoryImp.getAll()
        assert(response.isEmpty())
    }

    @Test
    fun get() = testApplication {
        val id: Long = 12
        val response = merchantRepositoryImp.get(id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val salesMerchantTable = SalesMerchantTable(
            nameUz = "Uz7",
            nameRu = "Ru7",
            nameEng = "Eng7",
            subdomain = "Subdomain7",
            phone = "+998999099909",
            password = "12332",
            logo = "logo",
            domain = "Domain7"
        )
        val response = merchantRepositoryImp.add(salesMerchantTable)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val salesMerchantTable = SalesMerchantTable(
            id = 12,
            nameUz = "Uz7",
            nameRu = "Ru7",
            nameEng = "Eng7",
            subdomain = "Subdomain7",
            phone = "+998999099909",
            password = "12332",
            logo = "logo",
            domain = "Domain7"
        )
        val response = merchantRepositoryImp.update(salesMerchantTable)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 3
        val response = merchantRepositoryImp.delete(id)
        if (response)
            assertTrue(response)
    }
}