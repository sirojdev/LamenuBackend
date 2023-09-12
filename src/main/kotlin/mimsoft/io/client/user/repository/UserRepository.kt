package mimsoft.io.client.user.repository

import mimsoft.io.client.user.UserDto
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel

interface UserRepository {
    suspend fun getAll(merchantId: Long? = null, limit: Long? = null, offset: Long? = null): List<UserDto>
    suspend fun get(id: Long?, merchantId:Long? = null): UserDto?
    suspend fun get(phone: String?, merchantId: Long?): UserDto?
    suspend fun add(userDto: UserDto?): ResponseModel
    suspend fun update(userDto: UserDto): ResponseModel
    suspend fun delete(id: Long?, merchantId: Long? = null): Boolean
}