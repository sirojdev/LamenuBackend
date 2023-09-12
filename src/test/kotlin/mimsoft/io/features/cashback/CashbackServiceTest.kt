package mimsoft.io.features.cashback

import io.ktor.server.testing.*
import mimsoft.io.utils.TextModel
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CashbackServiceTest {

    val cashbackService = CashbackService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 6
        val response = cashbackService.getAll(merchantId)
        assertNotNull(response)
        assert(response[0].id != null)
    }

    @Test
    fun add() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val cashbackDto = CashbackDto(
            merchantId = 1,
            name = textModel,
            minCost = 12.5,
            maxCost = 34.9
        )
        val response = cashbackService.add(cashbackDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "StringUzz",
            ru = "StringRuu",
            eng = "StringEngg"
        )
        val cashbackDto = CashbackDto(
            id = 16,
            merchantId = 1,
            name = textModel,
            minCost = 12.5,
            maxCost = 34.9
        )
        val response = cashbackService.update(cashbackDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val merchantId: Long = 1
        val id: Long = 16
        val response = cashbackService.delete(id, merchantId)
        assertTrue(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 16
        val response = cashbackService.get(merchantId, id)
        assertNotNull(response)
    }
}