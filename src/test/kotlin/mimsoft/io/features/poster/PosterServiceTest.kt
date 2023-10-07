package mimsoft.io.features.poster

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.features.pos.poster.PosterDto
import mimsoft.io.features.pos.poster.PosterService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PosterServiceTest {


    val posterService = PosterService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 55
        val response = posterService.get(merchantId)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val posterDto = PosterDto(
            merchantId = 1,
            selected = "sekected777",
            jowiApiKey = "jowi777",
            rKeeperClientId = 2,
            joinPosterApiKey = "JoinPoster777",
            rKeeperClientSecret = "Secret777"
        )
        val response = posterService.add(posterDto)
        println("rs: $response")
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val posterDto = PosterDto(
            merchantId = 1111,
            selected = "sekected77",
            jowiApiKey = "jowi77",
            rKeeperClientId = 2,
            joinPosterApiKey = "JoinPoster77",
            rKeeperClientSecret = "Secret77"
        )
        val response = posterService.update(posterDto)
        if (response)
            assertTrue(response)
    }
}