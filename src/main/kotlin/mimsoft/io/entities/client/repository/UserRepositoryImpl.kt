package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.USER_TABLE_NAME
import mimsoft.io.entities.client.UserDto
import mimsoft.io.entities.client.UserMapper
import mimsoft.io.entities.client.UserTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import mimsoft.io.utils.plugins.GSON

object UserRepositoryImpl : UserRepository {

    val repository: BaseRepository = DBManager
    val mapper = UserMapper
    override suspend fun getAll(): List<UserDto?> =
        DBManager.getData(dataClass = UserTable::class, tableName = USER_TABLE_NAME)
            .filterIsInstance<UserTable?>().map { mapper.toUserDto(it) }

    override suspend fun get(id: Long?): UserDto? =
        DBManager.getData(dataClass = UserTable::class, id = id, tableName = USER_TABLE_NAME)
            .firstOrNull().let { mapper.toUserDto(it as UserTable) }


    override suspend fun get(phone: String?): UserDto? {
        return DBManager.getPageData(
            dataClass = UserTable::class,
            tableName = USER_TABLE_NAME,
            where = mapOf("phone" to phone as Any)
        )?.data?.firstOrNull()?.let { mapper.toUserDto(it) }
    }

    override suspend fun add(userDto: UserDto?): ResponseModel {
        when {
            userDto?.phone == null -> return ResponseModel(
                httpStatus = PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = FIRSTNAME_NULL
            )
        }

        val oldUser = get(userDto?.phone)

        if (oldUser != null) return ResponseModel(
            httpStatus = ALREADY_EXISTS
        )

        return ResponseModel(
            body = DBManager.postData(
                dataClass = UserTable::class,
                dataObject = mapper.toUserTable(userDto),
                tableName = USER_TABLE_NAME
            ),
            httpStatus = OK
        )
    }

    override suspend fun update(userDto: UserDto?): ResponseModel {
        when {
            userDto?.phone == null -> return ResponseModel(
                httpStatus = PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = FIRSTNAME_NULL
            )
        }

        val oldUser = get(userDto?.phone)

        if (oldUser != null) return ResponseModel(
            httpStatus = ALREADY_EXISTS
        )

        return ResponseModel(
            body = DBManager.updateData(
                dataClass = UserTable::class,
                dataObject = mapper.toUserTable(userDto),
                tableName = USER_TABLE_NAME
            ),
            httpStatus = OK
        )
    }

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = USER_TABLE_NAME, whereValue = id)
}