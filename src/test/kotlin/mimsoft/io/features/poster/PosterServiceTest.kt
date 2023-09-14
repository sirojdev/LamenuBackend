package mimsoft.io.features.poster

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PosterServiceTest {


    val posterService = PosterService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val response = posterService.get(merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val posterDto = PosterDto(
            merchantId = 1,
            selected = "",
            jowiApiKey = "",
            rKeeperClientId = 2,
            joinPosterApiKey = "",
            rKeeperClientSecret = ""
        )
        val response = posterService.add(posterDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val posterDto = PosterDto(
            merchantId = 1,
            selected = "",
            jowiApiKey = "",
            rKeeperClientId = 2,
            joinPosterApiKey = "",
            rKeeperClientSecret = ""
        )
        val response = posterService.update(posterDto)
        assertTrue(response)
    }
}