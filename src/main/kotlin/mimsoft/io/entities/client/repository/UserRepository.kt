package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.UserTable

interface UserRepository {
    suspend fun getAll(): List<UserTable?>
    suspend fun get(id: Long?): UserTable?
    suspend fun get(phone: String?): UserTable?
    suspend fun add(userTable: UserTable?): Long?
    suspend fun update(userTable: UserTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}