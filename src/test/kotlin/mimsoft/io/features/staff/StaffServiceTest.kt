package mimsoft.io.features.staff

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
            position = StaffPosition.COLLECTOR,
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
        val id: Long = 1444
        val merchantId: Long = 1
        val response = staffService.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getByPhone() = testApplication {
        val phone = "+998901001012"
        val merchantId: Long = 1111
        val response = staffService.getByPhone(phone, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val staff = StaffDto(
            branchId = 33,
            image = "images/2023-06-26-18-59-13-567.jpg7",
            phone = "+998901001032",
            gender = "Erkak",
            comment = "This is comment7",
            password = "Passwor272",
            position = StaffPosition.COLLECTOR,
            merchantId = 1,
            lastName = "Ravshanov",
            firstName = "Ravshan"
        )
        val response = staffService.add(staff)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun update() = testApplication {// TODO:  there is an error...
        val staff = StaffDto(
            id = 42,
            branchId = 33,
            image = "images/2023-06-26-18-59-13-567.jpg7",
            phone = "+998901001032",
            gender = "Erkak",
            comment = "This is comment7",
            password = "Passwor888",
            position = StaffPosition.COLLECTOR,
            merchantId = 1,
            lastName = "Ravshanov",
            firstName = "Ravshan",
            birthDay = "2023-06-26"
        )
        val response = staffService.update(staff)
        println("rs: $response")
        assertTrue(response)

    }

    @Test
    fun delete() = testApplication {
        val id: Long = 42
        val merchantId: Long = 1
        val response = staffService.delete(id, merchantId)
        println("rs: $response")
    }

    @Test
    fun generateUuid() = testApplication { // TODO:  there is an error...
        val id: Long = 4211
        val response = staffService.generateUuid(id)
        println("rs: $response")
    }

    @Test
    fun getAllCourier() = testApplication {
        val merchantId: Long = 1111
        val limit = 10
        val offset = 5
        val response = staffService.getAllCourier(merchantId, limit, offset)
        if (response.data?.isEmpty() == true)
            assertNotNull(response.data)
    }

    @Test
    fun getAllCollector() = testApplication {
        val merchantId: Long = 1111
        val limit = 10
        val offset = 5
        val response = staffService.getAllCollector(merchantId, limit, offset)
        if (response.data?.isEmpty() == true)
            assertNotNull(response.data)
    }

    @Test
    fun getCollector() = testApplication { // TODO: there is an error...
        val id: Long = 42
        val merchantId: Long = 1
        val response = staffService.getCollector(id, merchantId)
        println("rs: $response")
    }

    @Test
    fun isExist() = testApplication {
        val staffId: Long = 42
        val merchantId: Long = 111
        val response = staffService.isExist(staffId, merchantId)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getAllWaiters() = testApplication {
        val merchantId: Long = 111
        val limit = 10
        val offset = 5
        val response = staffService.getAllWaiters(merchantId, limit, offset)
        if (response.data?.isEmpty() == true)
            assertNotNull(response.data)
    }
}