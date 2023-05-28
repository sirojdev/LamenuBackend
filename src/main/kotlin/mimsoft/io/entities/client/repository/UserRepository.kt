package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.UserDto
import mimsoft.io.entities.client.UserTable
import mimsoft.io.utils.ResponseModel

interface UserRepository {
    suspend fun getAll(): List<UserDto?>
    suspend fun get(id: Long?): UserDto?
    suspend fun get(phone: String?): UserDto?
    suspend fun add(userDto: UserDto?): ResponseModel
    suspend fun update(userDto: UserDto?): ResponseModel
    suspend fun delete(id: Long?): Boolean
}