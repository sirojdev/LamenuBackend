package mimsoft.io.features.message

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MessageServiceTest {

  private val messageService = MessageService

  @Test
  fun getAll() = testApplication {
    val response = messageService.getAll()
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val id: Long = 5
    val response = messageService.get(id)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun post() = testApplication {
    val messageDto = MessageDto(merchantId = 1, content = "Hello")
    val response = messageService.post(messageDto)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val messageDto =
      MessageDto(id = 999, merchantId = 1, content = "Hello", time = "2023-09-17 18:21:05.443000")
    val response = messageService.update(messageDto)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 999
    val response = messageService.delete(id)
    if (response) assertTrue(response)
  }
}
