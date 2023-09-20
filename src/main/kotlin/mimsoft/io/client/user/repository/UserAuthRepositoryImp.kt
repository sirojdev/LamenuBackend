package mimsoft.io.client.user.repository

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel

class UserAuthRepositoryImp : UserAuthRepository {
    override suspend fun registration(user: UserDto?): ResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun checkPhone(user: UserDto?): ResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun getWithPhone(phone: String?, merchantId: Long?): ResponseModel {
        TODO("Not yet implemented")
    }


}