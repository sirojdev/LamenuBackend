package mimsoft.io.features.manager_sys

import io.ktor.server.testing.*
import kotlin.test.Test


class ManagersServiceTest {

    @Test
    fun addManager() = testApplication {
        val model = ManagerSysModel(
            phone = "+998901001014",
            firstName = "Abdulhamid aka",
            lastName = "Tuqsonov",
            password = "12345678",
            image = "Image",
            role = ManagerSysRole.MANAGER
        )
        val response = ManagersService.addManager(model = model)
        println(response.body)
    }
}