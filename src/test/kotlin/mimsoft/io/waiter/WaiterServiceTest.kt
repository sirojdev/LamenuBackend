package mimsoft.io.waiter

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import mimsoft.io.features.staff.StaffDto

class WaiterServiceTest {

  @Test
  fun updateWaiterProfile() = testApplication {
    val staff =
      StaffDto(
        id = 41,
        firstName = "Update",
        lastName = "Update Surname",
        phone = "+998906084260",
        birthDay = "20.20.2020 10:10:10.000",
        image = "Update image",
        newPassword = "rootEmas",
        password = "123456"
      )
    val response = WaiterService.updateWaiterProfile(dto = staff)
    println("response = $response")
    assertNotNull(response)
  }
}
