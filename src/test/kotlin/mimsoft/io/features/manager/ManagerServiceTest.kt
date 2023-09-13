package mimsoft.io.features.manager

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class ManagerServiceTest {

    val managerService = ManagerService

    @Test
    fun getAll() = testApplication {
        val response = managerService.getAll()
        assertNotNull(response)
    }

    @Test
    fun getId() = testApplication {
        val id: Long = 12
        val response = managerService.get(id)
        assertNotNull(response)
    }

    @Test
    fun getPone() = testApplication {
        val phone = "+998900010101"
        val response = managerService.get(phone)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val managerDto = ManagerDto(
            firstName = "Kamoliddin",
            lastName = "Kamalov",
            phone = "998990077707",
            password = "security",
            token = "o2f2ofjajoddsioasdoik"
        )
        val response = managerService.add(managerDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val managerDto = ManagerDto(
            id = 2,
            firstName = "Kamoliddin",
            lastName = "Kamalov",
            phone = "998990077707",
            password = "security",
            token = "o2f2ofjajoddsioasdoik"
        )
        val response = managerService.update(managerDto)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 3
        val response = managerService.delete(id)
        assertNotNull(response)
        assertTrue(response)
    }
}