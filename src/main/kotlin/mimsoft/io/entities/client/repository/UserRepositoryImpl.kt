package mimsoft.io.entities.client.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.client.UserTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp

object UserRepositoryImpl : UserRepository {

    private const val selectAll =
        """SELECT *
           FROM users
           WHERE NOT deleted"""

    override suspend fun getAll(): List<UserTable> =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val users = mutableListOf<UserTable>()
            while (resultSet.next()) {
                users.add(
                    UserTable(
                        id = resultSet.getLong("id"),
                        phone = resultSet.getString("phone"),
                        firstName = resultSet.getString("first_name"),
                        lastName = resultSet.getString("last_name"),
                        image = resultSet.getString("image"),
                        birthDay = resultSet.getTimestamp("birth_day"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated"),
                        deleted = resultSet.getBoolean("deleted")
                    )
                )
            }
            return@withContext users
        }

    private const val selectById =
        """SELECT *
           FROM user
           WHERE NOT deleted
             AND id = ?"""

    override suspend fun get(id: Long?): UserTable? =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext UserTable(
                    id = resultSet.getLong("id"),
                    phone = resultSet.getString("phone"),
                    firstName = resultSet.getString("first_name"),
                    lastName = resultSet.getString("last_name"),
                    image = resultSet.getString("image"),
                    birthDay = resultSet.getTimestamp("birth_day"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated"),
                    deleted = resultSet.getBoolean("deleted")
                )
            } else {
                return@withContext null
            }
        }

    private const val insert =
        """INSERT INTO users
           (phone, first_name, last_name, image, birth_day, created)
           VALUES (?, ?, ?, ?, ?, ?)"""

    override suspend fun add(userTable: UserTable?): Long? =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection()
                .prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, userTable?.phone)
            statement.setString(2, userTable?.firstName)
            statement.setString(3, userTable?.lastName)
            statement.setString(4, userTable?.image)
            statement.setTimestamp(5, userTable?.birthDay)
            statement.setTimestamp(6, Timestamp(System.currentTimeMillis()))
            statement.execute()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    private const val update =
        """UPDATE user
           SET phone = ?,
               first_name = ?,
               last_name = ?,
               image = ?,
               birth_day = ?,
               updated = ?
           WHERE NOT deleted
             AND id = ?"""

    override suspend fun update(userTable: UserTable?): Boolean =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, userTable?.phone)
            statement.setString(2, userTable?.firstName)
            statement.setString(3, userTable?.lastName)
            statement.setString(4, userTable?.image)
            statement.setTimestamp(5, userTable?.birthDay)
            statement.setTimestamp(6, Timestamp(System.currentTimeMillis()))
            userTable?.id?.let { statement.setLong(7, it) }
            statement.execute()
            return@withContext true
        }

    private const val delete =
        """UPDATE user
           SET deleted = true
           WHERE NOT deleted
             AND id = ?"""

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(2, it) }
            statement.executeQuery()
            return@withContext true
        }
}