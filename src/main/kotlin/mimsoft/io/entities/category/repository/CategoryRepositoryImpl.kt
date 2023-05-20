package mimsoft.io.entities.category.repository

import mimsoft.io.entities.category.CATEGORY_TABLE_NAME
import mimsoft.io.entities.category.CategoryTable
import mimsoft.io.utils.DBManager

object CategoryRepositoryImpl : CategoryRepository {

    override suspend fun getAll(): List<CategoryTable?> {
        val data = DBManager.getData(dataClass = CategoryTable::class, tableName = CATEGORY_TABLE_NAME)
        return data.filterIsInstance<CategoryTable?>()
    }

    override suspend fun get(id: Long?): CategoryTable? =
        DBManager.getData(dataClass = CategoryTable::class, id = id, tableName = CATEGORY_TABLE_NAME).firstOrNull() as CategoryTable?

    override suspend fun add(categoryTable: CategoryTable?): Long? =
        DBManager.postData(dataClass = CategoryTable::class, dataObject = categoryTable, tableName = CATEGORY_TABLE_NAME)


    override suspend fun update(categoryTable: CategoryTable?): Boolean =
        DBManager.updateData(dataClass = CategoryTable::class, dataObject = categoryTable, tableName = CATEGORY_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = CATEGORY_TABLE_NAME, id = id)

}