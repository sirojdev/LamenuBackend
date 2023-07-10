package mimsoft.io.client.basket

import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserTable
import mimsoft.io.utils.ResponseModel

interface BasketRepository {
    suspend fun getAll(tgId:Long,merchantId: Long? = null): List<BasketDto?>
    suspend fun get(telegramId: Long?, merchantId:Long? = null,productId:Long?): BasketDto?
    suspend fun get(phone: String?, merchantId: Long?): UserDto?
    suspend fun add(basketDto: BasketDto): Long?
    suspend fun update(dto: BasketDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long? = null): Boolean
}