package mimsoft.io.features.notification.repository

import io.ktor.server.testing.*
import mimsoft.io.features.notification.NotificationDto
import kotlin.test.Test
import kotlin.test.assertEquals


class NotificationRepositoryImplTest {

    @Test
    fun getClient() = testApplication {
        val response = NotificationRepositoryImpl.getClient(1, 20)
        val response1 = response.get(0)
        assert(response1 is NotificationDto)
        println(response)
    }
}