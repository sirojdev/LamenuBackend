package mimsoft.io.features.client_promo

import io.ktor.server.testing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class ClientPromoServiceTest {

    val clientPromoService = ClientPromoService

    @Test
    fun add() = testApplication {
        val userDto = UserDto(
            id = 77
        )
        val promoDto = PromoDto(
            id = 66
        )
        val clientPromoDto = ClientPromoDto(
            client = userDto,
            promo = promoDto
        )
        val response = clientPromoService.add(clientPromoDto)
        assertNotNull(response)
    }

    @Test
    fun getByClientId() = testApplication {
        val clientId: Long = 21
        val response = clientPromoService.getByClientId(clientId)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = clientPromoService.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 12
        val response = clientPromoService.delete(id)
        assertTrue(response)
    }

    @Test
    fun check() = testApplication {// TODO: tekshirish kerak
        val promoName = "Name"
        val response = clientPromoService.check(promoName)
        assertNotNull(response)
    }
}