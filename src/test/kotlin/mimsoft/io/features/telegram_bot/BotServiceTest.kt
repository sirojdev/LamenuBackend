package mimsoft.io.features.telegram_bot

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BotServiceTest {


    private val botService = BotService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 7
        val response = botService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 5
        val merchantId: Long = 77
        val response = botService.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val botTable = BotTable(
            tgToken = "3ofajsodfjofjsdihfg",
            tgUsername = "Username77",
            groupId = "67",
            merchantId = 1,
        )
        val response = botService.add(botTable)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val botDto = BotDto(
            id = 7,
            tgToken = "3ofajsodfjofjsdihfg777",
            tgUsername = "Username777",
            groupId = "677",
            merchantId = 1
        )
        val response = botService.update(botDto)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val merchantId: Long = 1
        val id: Long = 7
        val response = botService.delete(id, merchantId)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }
}