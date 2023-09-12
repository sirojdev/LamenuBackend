package mimsoft.io.features.favourite

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.features.product.ProductDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FavouriteServiceTest {

    val favouriteService = FavouriteService

    @Test
    fun add() = testApplication {
        val productDto = ProductDto(
            id = 1
        )
        val favouriteDto = FavouriteDto(
            merchantId = 1,
            clientId = 26,
            deviceId = 11,
            product = productDto
        )
        val response = favouriteService.add(favouriteDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun move() = testApplication {
        val clientId: Long = 26
        val merchantId: Long = 1
        val deviceId: Long = 11
        val response = favouriteService.move(clientId, merchantId, deviceId)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val productDto = ProductDto(
            id = 1
        )
        val favouriteDto = FavouriteDto(
            merchantId = 1,
            clientId = 26,
            deviceId = 11,
            product = productDto
        )
        val response = favouriteService.update(favouriteDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getAll() = testApplication {
        val clientId: Long = 12
        val merchantId: Long = 1
        val response = favouriteService.getAll(clientId, merchantId)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 13
        val response = favouriteService.delete(id)
        assertTrue(response)
    }

    @Test
    fun deleteAll() = testApplication{
        val clientId : Long = 1
        val response = favouriteService.deleteAll(clientId)
        assertTrue(response)
    }
}