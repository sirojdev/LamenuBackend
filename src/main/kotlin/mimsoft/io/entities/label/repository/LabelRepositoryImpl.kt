package mimsoft.io.entities.label.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.label.LabelTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp

object LabelRepositoryImpl : LabelRepository {
    override suspend fun getAll(): List<LabelTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """SELECT * FROM label WHERE NOT deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val labels = mutableListOf<LabelTable?>()
            while (resultSet.next()) {
                labels.add(
                    LabelTable(
                        id = resultSet.getLong("id"),
                        menuId = resultSet.getLong("menu_id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        textColor = resultSet.getString("text_color"),
                        bgColor = resultSet.getString("bg_color"),
                        icon = resultSet.getString("icon"),
                        deleted = resultSet.getBoolean("deleted"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext labels
        }

    override suspend fun get(id: Long?): LabelTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """SELECT * FROM label WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext LabelTable(
                    id = resultSet.getLong("id"),
                    menuId = resultSet.getLong("menu_id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    textColor = resultSet.getString("text_color"),
                    bgColor = resultSet.getString("bg_color"),
                    icon = resultSet.getString("icon"),
                    deleted = resultSet.getBoolean("deleted"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(labelTable: LabelTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert = """INSERT INTO label (menu_id, name_uz, name_ru, name_eng, text_color, bg_color, icon, created)
VALUES (?, ?, ?, ?, ?, ?, ?, ?)"""
            val statement = DBManager.connection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            labelTable?.menuId?.let { statement.setLong(1, it) }
            statement.setString(2, labelTable?.nameUz)
            statement.setString(3, labelTable?.nameRu)
            statement.setString(4, labelTable?.nameEng)
            statement.setString(5, labelTable?.textColor)
            statement.setString(6, labelTable?.bgColor)
            statement.setString(7, labelTable?.icon)
            statement.setTimestamp(8, Timestamp(System.currentTimeMillis()))
            statement.execute()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            } else {
                return@withContext null
            }
        }

    override suspend fun update(labelTable: LabelTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """UPDATE label
SET menu_id = ?, name_uz = ?, name_ru = ?, name_eng = ?, text_color = ?, bg_color = ?, icon = ?, updated = ?
WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            labelTable?.menuId?.let { statement.setLong(1, it) }
            statement.setString(2, labelTable?.nameUz)
            statement.setString(3, labelTable?.nameRu)
            statement.setString(4, labelTable?.nameEng)
            statement.setString(5, labelTable?.textColor)
            statement.setString(6, labelTable?.bgColor)
            statement.setString(7, labelTable?.icon)
            statement.setTimestamp(8, Timestamp(System.currentTimeMillis()))
            labelTable?.id?.let { statement.setLong(9, it) }
            statement.executeUpdate()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """UPDATE label SET deleted = true WHERE NOT deleted AND id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.executeUpdate()
            return@withContext true
        }
}