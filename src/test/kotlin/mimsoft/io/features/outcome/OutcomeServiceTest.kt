package mimsoft.io.features.outcome

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.staff.StaffDto
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class OutcomeServiceTest {

    val outcomeObject = OutcomeService


    @Test
    fun getAll() = testApplication {
        val merchantId = 1L
        val response = outcomeObject.getAll(merchantId)
        assertNotNull(response)
        assert(response[0] is OutcomeTable)
    }

    @Test
    fun get() = testApplication {
        val merchantId = 1L
        val id = 11L
        val response = outcomeObject.get(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val outcomeDto = OutcomeDto(
            merchantId = 1
        )
        val response = outcomeObject.add(outcomeDto)
    }

    @Test
    fun update() = testApplication {
        val staffId = StaffDto(
            phone = "+998979779879"
        )
        val outcomeType = OutcomeTypeDto(
            id = 13
        )
        val outcomeDto = OutcomeDto(
            id = 11,
            name = "Sarvar",
            staff = staffId,
            outcomeType = outcomeType,
            merchantId = 1
        )
        val response = outcomeObject.update(outcomeDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val merchantId = 1L
        val id = 11L
        val response = outcomeObject.delete(merchantId, id)
        assertTrue(response)
    }
}