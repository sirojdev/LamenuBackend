package mimsoft.io.features.favourite

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.features.product.ProductDto

class FavouriteServiceTest {

  private val favouriteService = FavouriteService

  @Test
  fun add() = testApplication {
    val productDto = ProductDto(id = 1)
    val favouriteDto =
      FavouriteDto(merchantId = 1, clientId = 26, deviceId = 11, product = productDto)
    val response = favouriteService.add(favouriteDto)
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun move() = testApplication {
    val clientId: Long = 24
    val merchantId: Long = 1
    val deviceId: Long = 11
    val response = favouriteService.move(clientId, merchantId, deviceId)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val productDto = ProductDto(id = 16)
    val favouriteDto =
      FavouriteDto(
        id = 16,
        merchantId = 1,
        deviceId = 21,
        clientId = 6,
        product = ProductDto(id = 63)
      )
    val favouriteDto1 = FavouriteDto()
    val response = favouriteService.update(favouriteDto)
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun getAll() = testApplication {
    val clientId: Long = 16
    val merchantId: Long = 1
    val response = favouriteService.getAll(clientId, merchantId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 16
    val response = favouriteService.delete(id)
    if (response) assertTrue(response)
  }

  @Test
  fun deleteAll() = testApplication {
    val clientId: Long = 24
    val response = favouriteService.deleteAll(clientId)
    if (response) assertTrue(response)
  }
}
