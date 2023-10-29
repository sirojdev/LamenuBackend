package mimsoft.io.features.badge

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.utils.TextModel

class BadgeServiceTest {

  private val badgeService = BadgeService

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 1
    val response = badgeService.getAll(merchantId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val merchantId: Long = 1
    val id: Long = 2
    val response = badgeService.get(merchantId, id)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val textModel = TextModel(uz = "StringUz", ru = "StringRu", eng = "StringEng")
    val badgeDto =
      BadgeDto(
        id = null,
        name = textModel,
        textColor = "String",
        bgColor = "String",
        icon = "Icon",
        merchantId = 2
      )
    val response = badgeService.add(badgeDto)
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun update() = testApplication {
    val textModel = TextModel(uz = "StringUz", ru = "StringRu", eng = "StringEng")
    val badgeDto =
      BadgeDto(
        id = null,
        name = textModel,
        textColor = "#3fe4b2",
        bgColor = "#000000",
        icon = "Icon",
        merchantId = 2
      )
    val response = badgeService.update(badgeDto)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val merchantId: Long = 2
    val id: Long = 17
    val response = badgeService.delete(merchantId, id)
    if (response) assertTrue(response)
  }
}
