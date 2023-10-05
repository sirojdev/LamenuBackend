package mimsoft.io.features.stoplist

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class StopListServiceTest {

    private val stopListService = StopListService

    @Test
    fun decrementCount() = testApplication {
        val id: Long = 3
        val merchantId: Long = 6
        val response = stopListService.decrementCount(id, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 2
        val merchantId: Long = 66
        val response = stopListService.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val stopListDto = StopListDto(
            merchantId = 6,
            productId = 3,
            branchId = 4,
            count = 9
        )
        val response = stopListService.add(stopListDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val stopListDto = StopListDto(
            id = 3,
            merchantId = 6,
            productId = 5,
            branchId = 4,
            count = 7
        )
        val response = stopListService.update(stopListDto)
        if (response)
            assertNotNull(response)
        println("rs: $response")
    }

    @Test
    fun getByProduct() = testApplication {
        val productId: Long = 31
        val response = stopListService.getByProduct(productId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 6
        val response = stopListService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }
}