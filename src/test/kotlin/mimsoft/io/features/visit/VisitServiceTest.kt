package mimsoft.io.features.visit

import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.visit.enums.CheckStatus
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VisitServiceTest {

    private val visitService = VisitService

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 111
        val userId: Long = 21
        val response = visitService.getAll(merchantId, userId, 31)
        assert(response.isEmpty())
    }

    @Test
    fun add() = testApplication {
        val userDto = UserDto(
            id = 21
        )
        val staffDto = StaffDto(
            id = 21
        )
        val tableDto = TableDto(
            id = 13
        )
        val paymentTypeDto = PaymentTypeDto(
            id = 10
        )
        val checkStatus = CheckStatus.PAID
        val visitDto = VisitDto(
            merchantId = 1,
            user = userDto,
            waiter = staffDto,
            table = tableDto,
            status = checkStatus,
            payment = paymentTypeDto,
            price = 34.99,
            clientCount = 9
        )
        val response = visitService.add(visitDto)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 2912
        val merchantId: Long = 1
        val userId: Long = 21
        val response = visitService.get(id, merchantId, userId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val userDto = UserDto(
            id = 21
        )
        val staffDto = StaffDto(
            id = 21
        )
        val tableDto = TableDto(
            id = 13
        )
        val paymentTypeDto = PaymentTypeDto(
            id = 10
        )
        val checkStatus = CheckStatus.PAID
        val visitDto = VisitDto(
            id = 29,
            merchantId = 1,
            user = userDto,
            waiter = staffDto,
            table = tableDto,
            status = checkStatus,
            payment = paymentTypeDto,
            price = 34.9977,
            clientCount = 9
        )
        val response = visitService.update(visitDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 29
        val merchantId: Long = 1
        val response = visitService.delete(id, merchantId, 31)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }
}