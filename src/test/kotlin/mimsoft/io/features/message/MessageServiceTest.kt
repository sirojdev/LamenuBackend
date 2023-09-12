package mimsoft.io.features.message

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class MessageServiceTest {

    val messageService = MessageService

    @Test
    fun getAll() = testApplication {
        val response = messageService.getAll()
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 23
        val response = messageService.get(id)
        assertNotNull(response)
    }

    @Test
    fun post() = testApplication {
        val messageDto = MessageDto(
            merchantId = 1,
            content = "Hello"
        )
        val response = messageService.post(messageDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val messageDto = MessageDto(
            merchantId = 1,
            content = "Hello"
        )
        val response = messageService.update(messageDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 3
        val response = messageService.delete(id)
    }
}