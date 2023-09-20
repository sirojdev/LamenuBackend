package mimsoft.io.features.outcome_type

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.utils.toJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class OutcomeTypeServiceTest {

    val outcomeTypeObject = OutcomeTypeService

    @Test
    fun get() = testApplication {
        val merchantId: Long = 7
        val id: Long = 28
        val response = outcomeTypeObject.get(merchantId, id)
        assertNotNull(response)
        assert(response.id != null)
    }

    @Test
    fun add() = testApplication {// TODO:  OutcomeTypeDto id need to check
        val outcomeTypeDto = OutcomeTypeDto(
            merchantId = 7,
            name = "Abdusamad"
        )
        val response = outcomeTypeObject.add(outcomeTypeDto)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val outcomeTypeDto = OutcomeTypeDto(
            id = 24,
            merchantId = 7,
            name = "Outcome2"
        )
        val response = outcomeTypeObject.update(outcomeTypeDto)
        assertTrue(response)
    }

    @Test
    fun delete() {
        val id: Long = 24
        val merchantId: Long = 7
        val response = outcomeTypeObject.delete(merchantId, id)
        assertTrue(response)
    }

    @Test
     fun getUser() = testApplication {
        val response = UserRepositoryImpl.getAll(merchantId = 1, limit = 10, offset = 1)
        println(response.toJson())

    }
}