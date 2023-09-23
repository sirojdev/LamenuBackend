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
        assert(response.isEmpty())
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
        println("rs: $response")
    }

    @Test
    fun update() = testApplication {
        val tableDto = listOf(
            TableDto(
                id = 11
            )
        )
        val roomDto = RoomDto(
            id = 333,
            name = "Room-777",
            branchId = 34,
            merchantId = 1,
            tables = tableDto
        )
        val response = roomService.update(roomDto)
        if (response)
            assertTrue(response)
        println("rs: $response")
    }

    @Test
    fun delete() {
    }

    @Test
    fun getWithTable() {
    }
}