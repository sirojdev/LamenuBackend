package mimsoft.io.features.merchant.repository

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.features.merchant.MerchantTable

class MerchantRepositoryImpTest {

  private val merchantRepositoryImp = MerchantRepositoryImp

  @Test
  fun getInfo() = testApplication {
    val sub = "Teest"
    val response = merchantRepositoryImp.getInfo(sub)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getAll() = testApplication {
    val response = merchantRepositoryImp.getAll()
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val id: Long = 888
    val response = merchantRepositoryImp.get(id)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val merchantTable =
      MerchantTable(
        nameUz = "StringUz",
        nameRu = "StringRu",
        nameEng = "StringEng",
        sub = "Samsung",
        phone = "+998991010101",
        password = "1010101",
        logo = "Samsung logo"
      )
    val response = merchantRepositoryImp.add(merchantTable)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val merchantTable =
      MerchantTable(
        id = 1111,
        nameUz = "StringUz",
        nameRu = "StringRu",
        nameEng = "StringEng",
        sub = "Samsungg",
        phone = "+998991010101",
        password = "1010101",
        logo = "Samsung logo"
      )
    val response = merchantRepositoryImp.update(merchantTable)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 11
    val response = merchantRepositoryImp.delete(id)
    if (response) assertTrue(response)
  }

  @Test
  fun getMerchantById() = testApplication {
    val merchantId: Long = 1111
    val response = merchantRepositoryImp.getMerchantById(merchantId)
    if (response != null) assertNotNull(response)
  }
}
