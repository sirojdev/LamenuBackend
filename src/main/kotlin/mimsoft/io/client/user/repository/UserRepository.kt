package mimsoft.io.client.user.repository

import mimsoft.io.client.user.UserDto
import mimsoft.io.utils.ResponseModel

interface UserRepository {
    suspend fun getAll(): List<UserDto?>
    suspend fun get(id: Long?): UserDto?
    suspend fun get(phone: String?, merchantId: Long?): UserDto?
    suspend fun add(userDto: UserDto?): ResponseModel
    suspend fun update(userDto: UserDto?): ResponseModel
    suspend fun delete(id: Long?): Boolean
}