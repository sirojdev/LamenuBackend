package mimsoft.io.features.table

import io.ktor.server.testing.*
import io.ktor.server.util.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.visit.VisitDto
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TableServiceTest {

    private val tableService = TableService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 111
        val response = tableService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 17
        val response = tableService.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getByRoomId() = testApplication {
        val roomId: Long = 17
        val merchantId: Long = 111
        val response = tableService.getByRoomId(roomId, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val tableTable = TableTable(
            name = "TableName",
            roomId = 17,
            qr = "qrCode77",
            branchId = 31,
            type = 6,
            bookingTime = 30,
            status = "OPEN",
            merchantId = 1
        )
        val response = tableService.add(tableTable)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val roomDto = RoomDto(
            id = 17
        )
        val branchDto = BranchDto(
            id = 31
        )
        val tableStatus = TableStatus.OPEN
        val tableDto = TableDto(
            id = 23,
            qr = "qrCode77",
            name = "TableName77",
            type = 6,
            room = roomDto,
            branch = branchDto,
            merchantId = 1,
            status = tableStatus
        )
        val response = tableService.update(tableDto)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }

    @Test
    fun getRoomWithTables() = testApplication {
        val merchantId: Long = 1
        val branchId: Long = 31
        val response = tableService.getRoomWithTables(merchantId, branchId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 23
        val merchantId: Long = 1
        val response = tableService.delete(id, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getByQr() = testApplication {
        val url = ""
        val response = tableService.getByQr(url)
        if (response != null)
            assertNotNull(response)
    }
}