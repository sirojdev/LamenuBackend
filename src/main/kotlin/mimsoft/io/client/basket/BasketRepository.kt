package mimsoft.io.client.basket

import mimsoft.io.client.user.UserDto

interface BasketRepository {
  suspend fun getAll(telegramId: Long, merchantId: Long? = null): List<BasketDto?>

  suspend fun get(telegramId: Long?, merchantId: Long? = null, productId: Long?): BasketDto?

  suspend fun get(phone: String?, merchantId: Long?): UserDto?

  suspend fun add(dto: BasketDto): Long?

  suspend fun update(dto: BasketDto): Boolean

  suspend fun delete(id: Long?, merchantId: Long? = null): Boolean
}
