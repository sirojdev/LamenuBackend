package mimsoft.io.features.telephony

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TelephonyServiceTest {

  private val telephonyService = TelephonyService

  @Test
  fun getAll() = testApplication {
    val response = telephonyService.getAll()
    assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val merchantId: Long = 111
    val response = telephonyService.get(merchantId)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val telephonyDto =
      TelephonyDto(
        merchantId = 3,
        onlinePbxToken = "sdkcb3r2k32krjbfdk23rbk32e23eeqweqwe",
        selected = "Yes"
      )
    val response = telephonyService.add(telephonyDto)
    println("rs: $response")
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun update() = testApplication {
    val telephonyDto =
      TelephonyDto(
        merchantId = 3,
        onlinePbxToken = "sdkcb3r2k32krjbfdk23rbk32e23eeqweqwe",
        selected = "Yes"
      )
    val response = telephonyService.update(telephonyDto)
    println("rs: $response")
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val merchantId: Long = 3
    val response = telephonyService.delete(merchantId)
    println("rs: $response")
    if (response) assertTrue(response)
  }
}
