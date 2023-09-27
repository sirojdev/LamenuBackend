package mimsoft.io.features.room

import io.ktor.server.testing.*
import mimsoft.io.features.table.TableDto
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RoomServiceTest {

    private val roomService = RoomService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 111
        val response = roomService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 3011
        val merchantId: Long = 1
        val response = roomService.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val roomTable = RoomTable(
            name = "Romm-77",
            branchId = 33,
            merchantId = 1
        )
        val response = roomService.add(roomTable)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val tableDto = listOf(
            TableDto(
                id = 11
            )
        )
        val roomDto = RoomDto(
            id = 3444,
            name = "Room-777",
            branchId = 34,
            merchantId = 1,
            tables = tableDto
        )
        val response = roomService.update(roomDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 34
        val merchantId: Long = 1
        val response = roomService.delete(id, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getWithTable() = testApplication {
        val branchId: Long = 344
        val merchantId: Long = 1
        val response = roomService.getWithTable(branchId, merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }
}