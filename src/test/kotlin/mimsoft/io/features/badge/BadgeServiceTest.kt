package mimsoft.io.features.badge

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BadgeServiceTest {

    val badgeServiceObject = BadgeService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = badgeServiceObject.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 2
        val response = badgeServiceObject.get(merchantId, id)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val badgeDto = BadgeDto(
            id = null,
            name = textModel,
            textColor = "String",
            bgColor = "String",
            icon = "Icon",
            merchantId = 2
        )
        val response = badgeServiceObject.add(badgeDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val badgeDto = BadgeDto(
            id = null,
            name = textModel,
            textColor = "#3fe4b2",
            bgColor = "#000000",
            icon = "Icon",
            merchantId = 2
        )
        val response = badgeServiceObject.update(badgeDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val merchantId: Long = 2
        val id: Long = 17
        val response = badgeServiceObject.delete(merchantId, id)
        assertTrue(response)
    }
}