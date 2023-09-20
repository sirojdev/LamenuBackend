package mimsoft.io.features.book.repository


import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.utils.TextModel
import org.junit.jupiter.api.Assertions.*
import java.sql.Timestamp
import kotlin.test.Test

class BookRepositoryImplTest {

    val bookRepositoryImplObject = BookRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = bookRepositoryImplObject.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getAllClient() = testApplication {
        val merchantId: Long = 1
        val clientId: Long = 21
        val response = bookRepositoryImplObject.getAllClient(merchantId, clientId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 8
        val merchantId: Long = 1
        val userId: Long = 21
        val response = bookRepositoryImplObject.get(id, merchantId, userId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val bookStatus = BookStatus.ACCEPTED
        val branchId = BranchDto(id = 12)
        val clientId = UserDto(id = 9)
        val tableId = TableDto(id = 21)
        val bookDto = BookDto(
            id = null,
            merchantId = 6,
            branch = branchId,
            client = clientId,
            table = tableId,
            time = null,
            comment = "This is comment",
            visitorCount = 2,
            status = bookStatus
        )
        val response = bookRepositoryImplObject.add(bookDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val branchId = BranchDto(id = 12)
        val clientId = UserDto(id = 9)
        val tableId = TableDto(id = 21)
        val bookDto = BookDto(
            merchantId = 6,
            id = 34,
            client = clientId,
            table = tableId,
            time = null
        )
        val response = bookRepositoryImplObject.update(bookDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val userId: Long = 9
        val id: Long = 34
        val response = bookRepositoryImplObject.delete(id, userId)
        assertTrue(response)
    }

    @Test
    fun getAllMerchantBook() = testApplication {
        val merchantId: Long = 1
        val response = bookRepositoryImplObject.getAllMerchantBook(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getMerchantBook() = testApplication {
        val id: Long = 34
        val merchantId: Long = 6
        val response = bookRepositoryImplObject.getMerchantBook(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun addMerchantBook() = testApplication {
        val bookStatus = BookStatus.ACCEPTED
        val branchId = BranchDto(id = 12)
        val clientId = UserDto(id = 9)
        val tableId = TableDto(id = 21)
        val bookDto = BookDto(
            id = null,
            merchantId = 6,
            branch = branchId,
            client = clientId,
            table = tableId,
            time = null,
            comment = "This is comment",
            visitorCount = 2,
            status = bookStatus
        )
        val response = bookRepositoryImplObject.addMerchantBook(bookDto)
        assertNotNull(response)
    }

    @Test
    fun updateMerchantBook() = testApplication {
        val tableId = TableDto(id = 21)
        val bookDto = BookDto(
            id = 37,
            merchantId = 6,
            table = tableId,
            time = null,
            comment = "This is comment"
        )
        val response = bookRepositoryImplObject.updateMerchantBook(bookDto)
        assertTrue(response)
    }

    @Test
    fun deleteMerchantBook() = testApplication {
        val id: Long = 37
        val merchantId: Long = 6
        val response = bookRepositoryImplObject.deleteMerchantBook(id, merchantId)
        assertTrue(response)
    }

    @Test
    fun toAccepted() = testApplication {
        val merchantId: Long = 6
        val bookId: Long = 37
        val response = bookRepositoryImplObject.toAccepted(merchantId, bookId)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}