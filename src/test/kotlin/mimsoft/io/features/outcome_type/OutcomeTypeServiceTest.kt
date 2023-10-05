package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.utils.TextModel
import mimsoft.io.utils.toJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class OutcomeTypeServiceTest {

    private val outcomeTypeObject = OutcomeTypeService

    @Test
    fun getByMerchantId() = testApplication {
        val merchantId: Long = 7
        val response = outcomeTypeObject.getByMerchantId(merchantId)
        assert(response.isEmpty())
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 7
        val id: Long = 24
        val response = outcomeTypeObject.get(merchantId, id)
        if (response != null)
            assertNotNull(response)
    }


    @Test
    fun add() = testApplication {
        val outcomeTypeDto = OutcomeTypeDto(
            merchantId = 9,
            name = TextModel(
                uz = "Abdusamad",
                ru = "Abdusamad",
                eng = "Abdusamad"
            )
        )
        val response = outcomeTypeObject.add(outcomeTypeDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val outcomeTypeDto = OutcomeTypeDto(
            id = 300,
            merchantId = 9,
            name = TextModel(
                uz = "Abdusamad",
                ru = "Abdusamad",
                eng = "Abdusamad"
            )
        )
        val response = outcomeTypeObject.update(outcomeTypeDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() {
        val id: Long = 30
        val merchantId: Long = 9
        val response = outcomeTypeObject.delete(merchantId, id)
        if (response)
            assertTrue(response)
    }
}