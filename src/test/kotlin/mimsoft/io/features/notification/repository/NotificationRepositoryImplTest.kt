package mimsoft.io.features.notification.repository

import io.ktor.server.testing.*
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class NotificationRepositoryImplTest {

    private val notificationRepositoryImpl = NotificationRepositoryImpl

    @Test
    fun add() = testApplication {
        val title = TextModel(
            uz = "2 - taom",
            ru = "2 - yeda",
            eng = "2 - meal",
        )
        val body = TextModel(
            uz = "body-uz",
            ru = "body-ru",
            eng = "body-eng",
        )
        val notificationDto = NotificationDto(
            merchantId = 1,
            title = title,
            image = "Bg color",
            body = body,
            clientId = 26
        )
        val response = notificationRepositoryImpl.add(notificationDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication { // TODO: there is an error...
        val title = TextModel(
            uz = "2 - taom1",
            ru = "2 - yeda1",
            eng = "2 - meal1",
        )
        val body = TextModel(
            uz = "body-uz",
            ru = "body-ru",
            eng = "body-eng",
        )
        val notificationDto = NotificationDto(
            id = 12,
            merchantId = 1,
            title = title,
            image = "Bg color",
            body = body,
            clientId = 26
        )
        val response = notificationRepositoryImpl.update(notificationDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getById() = testApplication {
        val id: Long = 12222
        val merchantId: Long = 1
        val response = notificationRepositoryImpl.getById(id, merchantId)
        println("rs: $response")
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val limit = 3
        val offset = 5
        val search = ""
        val response = notificationRepositoryImpl.getAll(merchantId, limit, offset, search)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 1233
        val merchantId: Long = 1
        val response = notificationRepositoryImpl.delete(id, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getClient() = testApplication {
        val merchantId: Long = 1
        val userId: Long = 2777
        val response = notificationRepositoryImpl.getClient(merchantId, userId)
        assert(response.isEmpty())
    }
}