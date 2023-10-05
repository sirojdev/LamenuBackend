package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class StaffServiceTest {

    private val staffService = StaffService

    @Test
    fun auth() = testApplication {
        val staff = StaffDto(
            id = 14,
            token = null,
            branchId = 31,
            image = "images/2023-06-26-18-59-13-567.jpg",
            phone = "+998901001012",
            gender = "Erkak",
            status = true,
            comment = "This is comment",
            birthDay = null,
            password = "Passwor2",
            newPassword = null,
            position = "collector",
            merchantId = 1,
            lastName = "Name2",
            firstName = "Name1",
            allOrderCount = null,
            todayOrderCount = null,
            orders = listOf(),
            activeOrderCount = null,
            lastLocation = null
        )
        val staff2 = StaffDto(
            password = "Passwor2",
            phone = "+998901001012"
        )
        val response = staffService.auth(staff2)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = staffService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun getById() = testApplication {
        val id: Long = 14444
        val response = staffService.getById(id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 14
        val merchantId: Long = 1
        val response = staffService.get(id, merchantId)
        println("rs: $response")

    }

    @Test
    fun getByPhone() {
    }

    @Test
    fun add() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun generateUuid() {
    }

    @Test
    fun getAllCourier() {
    }

    @Test
    fun getAllCollector() {
    }

    @Test
    fun getCollector() {
    }

    @Test
    fun isExist() {
    }

    @Test
    fun getAllWaiters() {
    }
}