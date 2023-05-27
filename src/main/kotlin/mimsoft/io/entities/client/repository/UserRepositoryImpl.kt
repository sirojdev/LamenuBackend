package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.USER_TABLE_NAME
import mimsoft.io.entities.client.UserTable
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.Status
import mimsoft.io.utils.StatusCode
import mimsoft.io.utils.plugins.GSON

object UserRepositoryImpl : UserRepository {

    val status: Status = Status()

    override suspend fun getAll(): List<UserTable?> =
        DBManager.getData(dataClass = UserTable::class, tableName = USER_TABLE_NAME)
            .filterIsInstance<UserTable?>()

    override suspend fun get(id: Long?): UserTable? =
        DBManager.getData(dataClass = UserTable::class, id = id, tableName = USER_TABLE_NAME)
            .firstOrNull() as UserTable?


    override suspend fun get(phone: String?): UserTable? {
        return DBManager.getPageData(
            dataClass = UserTable::class,
            tableName = USER_TABLE_NAME,
            where = mapOf("phone" to phone as Any)
        )?.data?.firstOrNull()
    }

    override suspend fun add(userTable: UserTable?): Status {
        return when{
            get(userTable?.phone) != null -> {
                Status(
                    status = StatusCode.ALREADY_EXISTS
                )
            }

            userTable?.phone == null || userTable.firstName == null -> {
                Status(
                    status = StatusCode.PHONE_OR_FIRSTNAME_NULL
                )
            }

            else -> {
                println("\nadd user-->${GSON.toJson(userTable)}")
                Status(
                    body = DBManager.postData(
                        dataClass = UserTable::class,
                        dataObject = userTable,
                        tableName = USER_TABLE_NAME
                    ),
                    status = StatusCode.OK
                )
            }
        }
    }

    override suspend fun update(userTable: UserTable?): Status {
        return when{
            get(userTable?.phone) != null -> {
                Status(
                    status = StatusCode.ALREADY_EXISTS
                )
            }

            userTable?.phone == null || userTable.firstName == null -> Status(
                status = StatusCode.PHONE_OR_FIRSTNAME_NULL
            )

            else -> {
                DBManager.updateData(
                    dataClass = UserTable::class,
                    dataObject = userTable,
                    tableName = USER_TABLE_NAME
                )
                Status(
                    status = StatusCode.OK
                )
            }
        }
    }

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = USER_TABLE_NAME, whereValue = id)
}