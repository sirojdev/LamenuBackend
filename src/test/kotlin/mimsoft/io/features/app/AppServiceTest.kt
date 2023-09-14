package mimsoft.io.features.app

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.entities.app.AppService
import kotlin.test.assertNotNull

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppServiceTest {

    val appService = AppService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = appService.get(merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val appDto = AppDto(
            id = 1,
            merchantId = 8,
            googleToken = "TokenGoogle8",
            appleToken = "TokenApple8",
            telegramBotToken = "TokenTelegramBot8",
            selected = "String"
        )
        val response = appService.add(appDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val appDto = AppDto(
            merchantId = 5,
            googleToken = "TokenGoogle6",
            appleToken = "TokenApple6",
            telegramBotToken = "TokenTelegramBot6",
            selected = "String"
        )
        val response = appService.update(appDto)
        assertTrue(response)
    }
}