package mimsoft.io.features.book.repository


import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.BookStatus
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.table.TableDto
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class BookRepositoryImplTest {

    private val bookRepositoryImpl = BookRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = bookRepositoryImpl.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getAllClient() = testApplication {
        val merchantId: Long = 1
        val clientId: Long = 21
        val response = bookRepositoryImpl.getAllClient(merchantId, clientId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 8
        val merchantId: Long = 1
        val userId: Long = 21
        val response = bookRepositoryImpl.get(id, merchantId, userId)
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
        val response = bookRepositoryImpl.add(bookDto)
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
        val response = bookRepositoryImpl.update(bookDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val userId: Long = 9
        val id: Long = 34
        val response = bookRepositoryImpl.delete(id, userId)
        assertTrue(response)
    }

    @Test
    fun getAllMerchantBook() = testApplication {
        val merchantId: Long = 1
        val response = bookRepositoryImpl.getAllMerchantBook(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getMerchantBook() = testApplication {
        val id: Long = 34
        val merchantId: Long = 6
        val response = bookRepositoryImpl.getMerchantBook(id, merchantId)
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
        val response = bookRepositoryImpl.addMerchantBook(bookDto)
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
        val response = bookRepositoryImpl.updateMerchantBook(bookDto)
        assertTrue(response)
    }

    @Test
    fun deleteMerchantBook() = testApplication {
        val id: Long = 37
        val merchantId: Long = 6
        val response = bookRepositoryImpl.deleteMerchantBook(id, merchantId)
        assertTrue(response)
    }

    @Test
    fun toAccepted() = testApplication {
        val merchantId: Long = 6
        val bookId: Long = 37
        val response = bookRepositoryImpl.toAccepted(merchantId, bookId)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}