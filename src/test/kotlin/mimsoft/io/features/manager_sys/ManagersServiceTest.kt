package mimsoft.io.features.manager_sys

import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class FavouriteServiceTest {
    private val managersService = ManagersService

    @Test
    fun addManager() = testApplication {
        val managerSysRole = ManagerSysRole.MANAGER
        val managerSysModel = ManagerSysModel(
            phone = "+998901002000",
            password = "12345678",
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
        val id: Long = 7
        val phone = "+989990010101"
        val response = managersService.getManager(id, phone)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun getAllManager() = testApplication {
        val response = managersService.getAllManager()
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun deleteManager() = testApplication {
        val id: Long = 7
        val response = managersService.deleteManager(id)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }

    @Test
    fun updateManager() = testApplication {
        val managerSysRole = ManagerSysRole.MANAGER
        val managerSysModel = ManagerSysModel(
            phone = "+98999001010111",
            password = "12345677",
            firstName = "Salim",
            lastName = "Sarvarov77",
            image = "image",
            role = managerSysRole
        )
        val response = managersService.updateManager(managerSysModel)
        assertEquals(HttpStatusCode.OK, response.httpStatus)
    }
}