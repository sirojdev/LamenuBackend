package mimsoft.io.entities.category.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.category.CategoryTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp

object CategoryRepositoryImpl : CategoryRepository {

    override suspend fun getAll(): List<CategoryTable?> =
        withContext(Dispatchers.IO) {
            val selectAll =
                """select *
from category
where not deleted"""
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val categories = mutableListOf<CategoryTable?>()
            while (resultSet.next()) {
                categories.add(
                    CategoryTable(
                        id = resultSet.getLong("id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        image = resultSet.getString("image"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext categories
        }

    override suspend fun get(id: Long?): CategoryTable? =
        withContext(Dispatchers.IO) {
            val selectById =
                """select *
from category
where not deleted
  and id = ?"""
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext CategoryTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    image = resultSet.getString("image"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    override suspend fun add(categoryTable: CategoryTable?): Long? =
        withContext(Dispatchers.IO) {
            val insert =
                """insert into category
(name_uz,name_ru,name_eng,image,created)
values (?,?,?,?,?)"""
            val statement =DBManager.connection()
                .prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, categoryTable?.nameUz)
            statement.setString(2, categoryTable?.nameRu)
            statement.setString(3, categoryTable?.nameEng)
            categoryTable?.image?.let { statement.setString(4, it) }
            statement.setTimestamp(5, Timestamp(System.currentTimeMillis()))
            statement.executeQuery()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            }else null
        }

    override suspend fun update(categoryTable: CategoryTable?): Boolean =
        withContext(Dispatchers.IO) {
            val update =
                """update category
set name_uz   = ?,
    name_ru   = ?,
    name_eng  = ?,
    image     = ?,
    updated   = ?
where not deleted
  and id = ?"""
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, categoryTable?.nameUz)
            statement.setString(2, categoryTable?.nameRu)
            statement.setString(3, categoryTable?.nameEng)
            categoryTable?.image?.let { statement.setString(4, it) }
            statement.setTimestamp(5, Timestamp(System.currentTimeMillis()))
            categoryTable?.id?.let { statement.setLong(6, it) }
            statement.executeQuery()
            return@withContext true
        }

    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val delete =
                """update category
set deleted = true
where not deleted
  and id = ?"""
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            return@withContext true
        }
}