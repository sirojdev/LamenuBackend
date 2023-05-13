package mimsoft.io.entities.option.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.option.OptionTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp


object OptionRepositoryImpl : OptionRepository {
    override suspend fun getAll(): List<OptionTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """SELECT * FROM option WHERE NOT deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val options = mutableListOf<OptionTable?>()
            while (resultSet.next()) {
                options.add(
                    OptionTable(
                        id = resultSet.getLong("id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        descriptionUz = resultSet.getString("description_uz"),
                        descriptionRu = resultSet.getString("description_ru"),
                        descriptionEng = resultSet.getString("description_eng"),
                        image = resultSet.getString("image"),
                        price = resultSet.getDouble("price"),
                        deleted = resultSet.getBoolean("deleted"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext options
        }

    override suspend fun get(id: Long?): OptionTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """SELECT * FROM option WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext OptionTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    descriptionUz = resultSet.getString("description_uz"),
                    descriptionRu = resultSet.getString("description_ru"),
                    descriptionEng = resultSet.getString("description_eng"),
                    image = resultSet.getString("image"),
                    price = resultSet.getDouble("price"),
                    deleted = resultSet.getBoolean("deleted"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(optionTable: OptionTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert =
                """INSERT INTO option (name_uz, name_ru, name_eng, description_uz, description_ru, description_eng, image, price, created)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"""
            val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, optionTable?.nameUz)
            statement.setString(2, optionTable?.nameRu)
            statement.setString(3, optionTable?.nameEng)
            statement.setString(4, optionTable?.descriptionUz)
            statement.setString(5, optionTable?.descriptionRu)
            statement.setString(6, optionTable?.descriptionEng)
            statement.setString(7, optionTable?.image)
            statement.setDouble(8, optionTable?.price ?: 0.0)
            statement.setTimestamp(9, Timestamp(System.currentTimeMillis()))
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    override suspend fun update(optionTable: OptionTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """UPDATE option
SET name_uz = ?, name_ru = ?, name_eng = ?, description_uz = ?, description_ru = ?, description_eng = ?, image = ?, price = ?, updated = ?
WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, optionTable?.nameUz)
            statement.setString(2, optionTable?.nameRu)
            statement.setString(3, optionTable?.nameEng)
            statement.setString(4, optionTable?.descriptionUz)
            statement.setString(5, optionTable?.descriptionRu)
            statement.setString(6, optionTable?.descriptionEng)
            statement.setString(7, optionTable?.image)
            statement.setDouble(8, optionTable?.price ?: 0.0)
            statement.setTimestamp(9, Timestamp(System.currentTimeMillis()))
            optionTable?.id?.let { statement.setLong(10, it) }
            statement.executeUpdate()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """UPDATE option SET deleted = true WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.executeUpdate()
            return@withContext true
        }
}