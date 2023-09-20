package mimsoft.io.features.favourite

import io.ktor.server.testing.*
import mimsoft.io.features.product.ProductDto
import kotlin.test.Test

class FavouriteServiceTest{
    @Test
    fun move() = testApplication {
        val merchantId = 1L
        val deviceId = 5L
        val clientId = 26L
        val a = FavouriteService.move(clientId = clientId, merchantId = merchantId, deviceId = deviceId)
        println(a)
    }
    @Test
    fun add() = testApplication {
        val dto = FavouriteDto(
            merchantId = 1,
            deviceId = 21,
            clientId = 6,
            product = ProductDto(
                id = 63
            )
        )
        FavouriteService.add(favouriteDto = dto)
    }
}
