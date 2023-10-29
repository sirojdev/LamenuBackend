package mimsoft.io.features.client_promo

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto

class ClientPromoServiceTest {

  private val clientPromoService = ClientPromoService

  @Test
  fun add() = testApplication {
    val userDto = UserDto(id = 77)
    val promoDto = PromoDto(id = 66)
    val clientPromoDto = ClientPromoDto(client = userDto, promo = promoDto)
    val response = clientPromoService.add(clientPromoDto)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getByClientId() = testApplication {
    val clientId: Long = 211
    val response = clientPromoService.getByClientId(clientId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 11
    val response = clientPromoService.getAll(merchantId)
    println("rs: $response")
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 7
    val response = clientPromoService.delete(id)
    if (response) assertTrue(response)
  }

  @Test
  fun check() = testApplication {
    val promoName = "kjbekjbw"
    val response = clientPromoService.check(promoName)
    if (response != null) assertNotNull(response)
  }
}
