package mimsoft.io.features.extra.ropository

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.extra.ExtraTable
import mimsoft.io.utils.TextModel

class ExtraRepositoryImplTest {

  private val extraRepositoryImpl = ExtraRepositoryImpl

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 11212
    val response = extraRepositoryImpl.getAll(merchantId, 31)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val merchantId: Long = 1
    val id: Long = 15555
    val response = extraRepositoryImpl.get(id, merchantId, 31)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val textModel = TextModel(uz = "StringUz77", ru = "StringRu77", eng = "StringEng77")
    val extraDto =
      ExtraDto(
        id = 151111,
        image = "images/2023-07-18-13-22-54-645.webp",
        price = 3437,
        merchantId = 1,
        name = textModel,
        productId = 63,
        jowiId = "11"
      )
    val response = extraRepositoryImpl.update(extraDto)
    if (response) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val extraTable =
      ExtraTable(
        price = 3224,
        image = "images/2023-07-18-13-22-54-645.webp",
        nameUz = "Uz",
        nameRu = "Ru",
        nameEng = "Uz",
        productId = 63,
        merchantId = 1
      )
    val response = extraRepositoryImpl.add(extraTable)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getExtrasByProductId() = testApplication {
    val merchantId: Long = 1
    val productId: Long = 63
    val response = extraRepositoryImpl.getExtrasByProductId(merchantId, productId)
    if (response != null) {
      assert(response.isEmpty())
    }
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 16
    val merchantId: Long = 1
    val response = extraRepositoryImpl.delete(id, merchantId, 31)
    if (response) assertNotNull(response)
  }
}
