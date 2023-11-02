package mimsoft.io.features.news.repository

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.features.news.NewsDto
import mimsoft.io.utils.TextModel

class NewsRepositoryImplTest {

  private val newsRepositoryImpl = NewsRepositoryImpl

  @Test
  fun add() = testApplication {
    val textModel =
      TextModel(
        uz = "Uz",
        ru = "Ru",
        eng = "Eng",
      )
    val newsDto = NewsDto(merchantId = 1, title = textModel)
    val response = newsRepositoryImpl.add(newsDto)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val textModel =
      TextModel(
        uz = "Uz",
        ru = "Ru",
        eng = "Eng",
      )
    val newsDto = NewsDto(id = 2444, merchantId = 1, title = textModel)
    val response = newsRepositoryImpl.update(newsDto)
    if (response) assertTrue(response)
  }

  @Test
  fun getById() = testApplication {
    val id: Long = 2444
    val merchantId: Long = 1
    val response = NewsRepositoryImpl.getById(id, merchantId)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 1
    val limit = 10
    val offset = 5
    val response = newsRepositoryImpl.getAll(merchantId, limit, offset)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 24444
    val merchantId: Long = 1
    val response = newsRepositoryImpl.delete(id, merchantId)
    if (response) assertTrue(response)
  }
}
