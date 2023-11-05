package mimsoft.io.client.user.repository

import mimsoft.io.client.user.UserDto
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel

interface UserRepository {

  suspend fun getAll(
    merchantId: Long? = null,
    search: String? = null,
    filters: String? = null,
    limit: Int? = null,
    offset: Int? = null,
  ): DataPage<UserDto>

  suspend fun add(userDto: UserDto?): ResponseModel

  suspend fun update(userDto: UserDto): Boolean

  suspend fun delete(id: Long?, merchantId: Long? = null): Boolean

  suspend fun get(id: Long?, merchantId: Long? = null): UserDto?

  suspend fun get(phone: String?, merchantId: Long?): UserDto?

  suspend fun search(phone: String?, merchantId: Long?): DataPage<UserDto>

  suspend fun updatePhone(userId: Long?, phone: String?): Boolean

  suspend fun updateImage(imageName: String?, userId: Long?): Boolean
}
