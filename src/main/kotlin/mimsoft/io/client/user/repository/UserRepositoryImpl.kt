package mimsoft.io.client.user.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.USER_TABLE_NAME
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserMapper
import mimsoft.io.client.user.UserTable
import mimsoft.io.features.extra.EXTRA_TABLE_NAME
import mimsoft.io.features.extra.ExtraTable
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import java.sql.Timestamp

object UserRepositoryImpl : UserRepository {

    val repository: BaseRepository = DBManager
    val mapper = UserMapper
    override suspend fun getAll(merchantId: Long?): List<UserTable?> {
        val where = mutableMapOf<String, Any>()
        if (merchantId != null) where["merchant_id"] = merchantId
        val data = repository.getPageData(
            dataClass = UserTable::class,
            where = where,
            tableName = USER_TABLE_NAME
        )?.data
        return data ?: emptyList()
    }


    override suspend fun get(id: Long?, merchantId: Long?): UserDto? {
        val where = mutableMapOf<String, Any>()

        if (merchantId != null) {
            where["merchant_id"] = merchantId
            where["id"] = id as Any
        } else where["id"] = id as Any

        val data = repository.getPageData(
            dataClass = UserTable::class,
            where = where,
            tableName = EXTRA_TABLE_NAME
        )?.data?.firstOrNull()
        return UserMapper.toUserDto(data)
    }


    override suspend fun get(phone: String?, merchantId: Long?): UserDto? {
        return DBManager.getPageData(
            dataClass = UserTable::class,
            tableName = USER_TABLE_NAME,
            where = mapOf(
                "phone" to phone as Any,
                "merchant_id" to merchantId as Any
            )
        )?.data?.firstOrNull()?.let { mapper.toUserDto(it) }
    }

    override suspend fun add(userDto: UserDto?): ResponseModel {
        when {
            userDto?.phone == null -> return ResponseModel(
                httpStatus = ResponseModel.PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = ResponseModel.FIRSTNAME_NULL
            )

            userDto.merchantId == null -> return ResponseModel(
                httpStatus = ResponseModel.MERCHANT_ID_NULL
            )
        }

        val oldUser = get(userDto?.phone, userDto?.merchantId)

        if (oldUser != null) return ResponseModel(
            httpStatus = ResponseModel.ALREADY_EXISTS
        )

        return ResponseModel(
            body = DBManager.postData(
                dataClass = UserTable::class,
                dataObject = mapper.toUserTable(userDto),
                tableName = USER_TABLE_NAME
            ),
            httpStatus = ResponseModel.OK
        )
    }

    override suspend fun update(userDto: UserDto): ResponseModel {
        when {
            userDto.phone == null -> return ResponseModel(
                httpStatus = ResponseModel.PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = ResponseModel.FIRSTNAME_NULL
            )
        }
        var query = "UPDATE $USER_TABLE_NAME " +
                "SET" +
                " phone = ?, " +
                " first_name = ?, " +
                " last_name = ?," +
                " image = ?," +
                " birth_day = ? ," +
                " badge_id = ${userDto.badge?.id}," +
                " updated = ?" +
                " WHERE not deleted and id = ${userDto.id} "
        if (userDto.merchantId!=null) {
            query += "and merchant_id = ${userDto.merchantId}"
        }
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, userDto.phone)
                    ti.setString(2, userDto.firstName)
                    ti.setString(3, userDto.lastName)
                    ti.setString(4, userDto.image)
                    ti.setTimestamp(5, Timestamp.valueOf(userDto.birthDay))
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(
            httpStatus = ResponseModel.OK
        )
    }

    override suspend fun delete(id: Long?, merchantId: Long?):Boolean{
        var query = "update $EXTRA_TABLE_NAME set deleted = true where id = $id "
        if(merchantId != null){
            query += "and merchant_id = $merchantId"
        }
        withContext(Dispatchers.IO) {
            ExtraRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}