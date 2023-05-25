package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.UserTable
import mimsoft.io.utils.Status

interface UserRepository {
    suspend fun getAll(): List<UserTable?>
    suspend fun get(id: Long?): UserTable?
    suspend fun get(phone: String?): UserTable?
    suspend fun add(userTable: UserTable?): Status
    suspend fun update(userTable: UserTable?): Status
    suspend fun delete(id: Long?): Boolean
}