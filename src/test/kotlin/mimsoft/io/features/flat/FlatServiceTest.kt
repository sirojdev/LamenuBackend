package mimsoft.io.features.flat

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FlatServiceTest {

    val flatService = FlatService

    @Test
    fun getAll() = testApplication {
        val response = flatService.getAll()
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 1
        val response = flatService.get(id)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val flatTable = FlatTable(
            name = "admin flat",
            branchId = 2,
            restaurantId = 2
        )
        val response = flatService.add(flatTable)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val flatTable = FlatTable(
            id = 1,
            name = "admin flat",
            branchId = 2,
            restaurantId = 2
        )
        val response = flatService.update(flatTable)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 1
        val response = flatService.delete(id)
        assertTrue(response)
    }
}