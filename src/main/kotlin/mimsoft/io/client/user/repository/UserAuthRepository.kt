package mimsoft.io.client.user.repository

import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserTable
import mimsoft.io.utils.ResponseModel

interface UserAuthRepository {
    suspend fun registration(user : UserDto?) : ResponseModel
    suspend fun checkPhone(user: UserDto?) : ResponseModel
    suspend fun getWithPhone(phone: String?, merchantId: Long?) : ResponseModel
}