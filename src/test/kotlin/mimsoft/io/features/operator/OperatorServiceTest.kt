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
        val merchantId: Long = 111
        val response = operatorService.getAll(merchantId)
        assert(response.isEmpty())
    }

    @Test
    fun get() = testApplication {
        val id: Long = 2
        val response = operatorService.get(id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getByPbxCode() = testApplication {
        val pbxCode = 1000
        val merchantId: Long = 1
        val response = operatorService.getByPbxCode(merchantId, pbxCode)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {// TODO: there is an error...
        val operator = Operator(
            staffId = 38,
            merchantId = 1,
            pbxCode = 101

        )
        val response = operatorService.add(operator)
        println("rs: $response")
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {
        val operator = Operator(
            staffId = 39,
            merchantId = 1
        )
        val response = operatorService.update(operator)
        println("rs: $response")
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 12
        val response = operatorService.delete(id)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}