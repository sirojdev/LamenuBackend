package mimsoft.io.entities.extra.ropository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.extra.ExtraTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp


object ExtraRepositoryImpl : ExtraRepository {
    override suspend fun getAll(): List<ExtraTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """SELECT * FROM extra WHERE NOT deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val extras = mutableListOf<ExtraTable?>()
            while (resultSet.next()) {
                extras.add(
                    ExtraTable(
                        id = resultSet.getLong("id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        price = resultSet.getDouble("price"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext extras
        }

    override suspend fun get(id: Long?): ExtraTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """SELECT * FROM extra WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext ExtraTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    price = resultSet.getDouble("price"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(extraTable: ExtraTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert =
                """INSERT INTO extra (name_uz, name_ru, name_eng, price, created)
VALUES (?, ?, ?, ?, ?)"""
            val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, extraTable?.nameUz)
            statement.setString(2, extraTable?.nameRu)
            statement.setString(3, extraTable?.nameEng)
            statement.setDouble(4, extraTable?.price ?: 0.0)
            statement.setTimestamp(5, Timestamp(System.currentTimeMillis()))
            statement.execute()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    override suspend fun update(extraTable: ExtraTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """UPDATE extra
SET name_uz = ?, name_ru = ?, name_eng = ?, price = ?, updated = ?
WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, extraTable?.nameUz)
            statement.setString(2, extraTable?.nameRu)
            statement.setString(3, extraTable?.nameEng)
            statement.setDouble(4, extraTable?.price ?: 0.0)
            statement.setTimestamp(5, Timestamp(System.currentTimeMillis()))
            extraTable?.id?.let { statement.setLong(6, it) }
            statement.executeUpdate()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """UPDATE extra SET deleted = true WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.executeUpdate()
            return@withContext true
        }
}