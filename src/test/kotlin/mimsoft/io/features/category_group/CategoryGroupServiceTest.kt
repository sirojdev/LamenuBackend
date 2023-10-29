package mimsoft.io.features.category_group

import io.ktor.server.testing.*
import kotlin.test.Test
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.utils.TextModel
import org.junit.jupiter.api.Assertions.*

class CategoryGroupServiceTest {

  private val categoryGroupService = CategoryGroupService

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 1
    val response = categoryGroupService.getAll(merchantId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val textModel = TextModel(uz = "StringUz", ru = "StringRu", eng = "StringEng")
    val categoryDto = CategoryDto(id = 2)
    val categoryGroupDto =
      CategoryGroupDto(
        merchantId = 1,
        title = textModel,
        bgColor = "#EAF4AE",
        priority = 1,
      )
    val response = categoryGroupService.add(categoryGroupDto)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val textModel = TextModel(uz = "Uz", ru = "Ru", eng = "Eng")
    val categoryGroupDto =
      CategoryGroupDto(
        id = 12,
        merchantId = 1,
        title = textModel,
        bgColor = "#EAF4AE",
        priority = 1,
      )
    val response = categoryGroupService.update(categoryGroupDto)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val merchantId: Long = 1
    val id: Long = 12
    val response = categoryGroupService.delete(id, merchantId)
    if (response) assertTrue(response)
  }

  @Test
  fun getById() = testApplication {
    val merchantId: Long = 1
    val id: Long = 12
    val response = categoryGroupService.getById(merchantId, id)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getClient() = testApplication {
    val merchantId: Long = 1
    val response = categoryGroupService.getClient(merchantId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun getCategoryGroupById() = testApplication {
    val merchantId: Long = 1
    val id: Long = 12
    val response = categoryGroupService.getCategoryGroupById(merchantId, id)
    if (response != null) assertNotNull(response)
  }
}
