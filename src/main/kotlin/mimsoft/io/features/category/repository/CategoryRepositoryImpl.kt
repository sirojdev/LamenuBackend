package mimsoft.io.features.category.repository

import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryMapper
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object CategoryRepositoryImpl : CategoryRepository {

    val repository: BaseRepository = DBManager
    val mapper = CategoryMapper

    override suspend fun getAll(): List<CategoryDto?> {
        val data = repository.getData(
            dataClass = CategoryTable::class,
            tableName = CATEGORY_TABLE_NAME)
        return data.filterIsInstance<CategoryTable?>().map { mapper.toCategoryDto(it) }
    }

    override suspend fun get(id: Long?): CategoryDto? =
        DBManager.getData(
            dataClass = CategoryTable::class,
            id = id, tableName = CATEGORY_TABLE_NAME)
            .firstOrNull().let { mapper.toCategoryDto(it as? CategoryTable) }

    override suspend fun add(categoryDto: CategoryDto?): Long? =
        DBManager.postData(dataClass = CategoryTable::class,
            dataObject = mapper.toCategoryTable(categoryDto),
            tableName = CATEGORY_TABLE_NAME)


    override suspend fun update(categoryDto: CategoryDto?): Boolean =
        DBManager.updateData(
            dataClass = CategoryTable::class,
            dataObject = mapper.toCategoryTable(categoryDto),
            tableName = CATEGORY_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = CATEGORY_TABLE_NAME, whereValue = id)

}