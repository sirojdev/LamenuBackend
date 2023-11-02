package mimsoft.io.features.outcome

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.staff.StaffDto
import org.junit.jupiter.api.Assertions.*

class OutcomeServiceTest {

  private val outcomeObject = OutcomeService

  @Test
  fun get() = testApplication {
    val merchantId: Long = 1
    val id: Long = 111
    val response = outcomeObject.get(id, merchantId)
    println("rs: $response")
    if (response != null) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val staffDto = StaffDto(id = 11, phone = "+998999090909")
    val outcomeTypeDto = OutcomeTypeDto(id = 17)
    val outcomeDto =
      OutcomeDto(merchantId = 1, name = "Kamol", staff = staffDto, outcomeType = outcomeTypeDto)
    val response = outcomeObject.add(outcomeDto)
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun update() = testApplication {
    val staffId = StaffDto(id = 17, phone = "+998979779879")
    val outcomeType = OutcomeTypeDto(id = 13)
    val outcomeDto =
      OutcomeDto(
        id = 111,
        name = "Sarvar1",
        staff = staffId,
        outcomeType = outcomeType,
        merchantId = 1
      )
    val response = outcomeObject.update(outcomeDto)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val merchantId: Long = 1
    val id: Long = 11
    val response = outcomeObject.delete(merchantId, id)
    if (response) assertTrue(response)
  }
}
