package mimsoft.io.entities.client.repository

import mimsoft.io.entities.client.USER_TABLE_NAME
import mimsoft.io.entities.client.UserTable
import mimsoft.io.utils.DBManager

object UserRepositoryImpl : UserRepository {

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

    override suspend fun add(userTable: UserTable?): Long? {
        return when{
            get(userTable?.phone) != null -> null

            userTable?.phone == null || userTable.firstName == null -> null

            else -> DBManager.postData(
                dataClass = UserTable::class,
                dataObject = userTable,
                tableName = USER_TABLE_NAME)
        }
    }

    override suspend fun update(userTable: UserTable?): Boolean {
        return when{
            get(userTable?.phone) != null -> false

            userTable?.phone == null || userTable.firstName == null -> false

            else -> DBManager.updateData(
                dataClass = UserTable::class,
                dataObject = userTable,
                tableName = USER_TABLE_NAME)
        }
    }

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = USER_TABLE_NAME, id = id)
}