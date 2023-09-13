package mimsoft.io.features.manager_sys

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ManagersServiceTest {

    val managersService = ManagersService

    @Test
    fun addManager() = testApplication {
        val managerSysRole = ManagerSysRole.MANAGER
        val managerSysModel = ManagerSysModel(
            phone = "+998901001011",
            password = "1234567",
            firstName = "Salim",
            lastName = "Sarvarov",
            image = "image",
            role = managerSysRole
        )
        val response = managersService.addManager(managerSysModel)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getManager() = testApplication {
        val id: Long = 3
        val phone = "+998901001011"
        val response = managersService.getManager(id, phone)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getAllManager() = testApplication {
        val response = managersService.getAllManager()
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun deleteManager() = testApplication {
        val id: Long = 4
        val response = managersService.deleteManager(id)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun updateManager() = testApplication {
        val managerSysRole = ManagerSysRole.MANAGER
        val managerSysModel = ManagerSysModel(
            phone = "+998901001011",
            password = "1234567",
            firstName = "Salim",
            lastName = "Sarvarov",
            image = "image",
            role = managerSysRole
        )
        val response = managersService.updateManager(managerSysModel)
        assertNotNull(response)
        assertEquals(HttpStatusCode.OK,response.httpStatus)
    }
}