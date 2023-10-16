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
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun getAllClient() = testApplication {
        val merchantId: Long = 1
        val clientId: Long = 21
        val response = bookRepositoryImpl.getAllClient(merchantId, clientId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 8
        val merchantId: Long = 1
        val userId: Long = 21
        val response = bookRepositoryImpl.get(id, merchantId, userId)
        if (response != null)
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
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val userId: Long = 9
        val id: Long = 34
        val response = bookRepositoryImpl.delete(id, userId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getAllMerchantBook() = testApplication {
        val merchantId: Long = 1
        val response = bookRepositoryImpl.getAllBranchBook(merchantId, 31)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun getMerchantBook() = testApplication {
        val id: Long = 34
        val merchantId: Long = 6
        val response = bookRepositoryImpl.getMerchantBook(id, merchantId, 31)
        if (response != null)
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
        if (response != null)
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
        val response = bookRepositoryImpl.updateBranchBook(bookDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun deleteMerchantBook() = testApplication {
        val id: Long = 37
        val merchantId: Long = 6
        val response = bookRepositoryImpl.deleteBranchBook(id, merchantId, 31)
        if (response)
            assertTrue(response)
    }

    @Test
    fun toAccepted() = testApplication {
        val merchantId: Long = 6
        val bookId: Long = 37
        val response = bookRepositoryImpl.toAccepted(merchantId, bookId, 31)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}