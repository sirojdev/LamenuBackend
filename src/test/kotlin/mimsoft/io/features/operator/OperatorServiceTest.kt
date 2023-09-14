package mimsoft.io.features.operator

import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.features.favourite.merchant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class OperatorServiceTest {

    val operatorService = OperatorService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = operatorService.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 32
        val response = operatorService.get(id)
        assertNotNull(response)
    }

    @Test
    fun getByPbxCode() = testApplication {
        val pbxCode = 1
        val merchantId: Long = 2
        val response = operatorService.getByPbxCode(merchantId, pbxCode)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val operator = Operator(
            staffId = 39,
            merchantId = 1
        )
        val response = operatorService.add(operator)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val operator = Operator(
            staffId = 39,
            merchantId = 1
        )
        val response = operatorService.update(operator)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 12
        val response = operatorService.delete(id)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}