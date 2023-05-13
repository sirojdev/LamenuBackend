package mimsoft.io.entities.menu.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.menu.MenuTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp


object MenuRepositoryImpl : MenuRepository {
    override suspend fun getAll(): List<MenuTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """SELECT * FROM menu WHERE NOT deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val menus = mutableListOf<MenuTable?>()
            while (resultSet.next()) {
                menus.add(
                    MenuTable(
                        id = resultSet.getLong("id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        deleted = resultSet.getBoolean("deleted"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext menus
        }

    override suspend fun get(id: Long?): MenuTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """SELECT * FROM menu WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext MenuTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    deleted = resultSet.getBoolean("deleted"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(menuTable: MenuTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert = """INSERT INTO menu (name_uz, name_ru, name_eng, created)
VALUES (?, ?, ?, ?)"""
            val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, menuTable?.nameUz)
            statement.setString(2, menuTable?.nameRu)
            statement.setString(3, menuTable?.nameEng)
            statement.setTimestamp(4, Timestamp(System.currentTimeMillis()))
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    override suspend fun update(menuTable: MenuTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """UPDATE menu
SET name_uz = ?, name_ru = ?, name_eng = ?, updated = ?
WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, menuTable?.nameUz)
            statement.setString(2, menuTable?.nameRu)
            statement.setString(3, menuTable?.nameEng)
            statement.setTimestamp(4, Timestamp(System.currentTimeMillis()))
            menuTable?.id?.let { statement.setLong(5, it) }
            statement.executeUpdate()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """UPDATE menu SET deleted = true WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.executeUpdate()
            return@withContext true
        }
}