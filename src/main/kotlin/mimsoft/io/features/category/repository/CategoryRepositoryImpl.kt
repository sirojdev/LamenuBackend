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

    override suspend fun getAllByMerchant(merchantId: Long?): List<CategoryDto?> {
        val data = repository.getPageData(
            dataClass = CategoryTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = "category"
        )?.data

        return data?.map { mapper.toCategoryDto(it) } ?: emptyList()
    }


    override suspend fun get(id: Long?, merchantId: Long?): CategoryDto? {
        val data = repository.getPageData(
            dataClass = CategoryTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = CATEGORY_TABLE_NAME
        )?.data?.firstOrNull()
        return mapper.toCategoryDto(data)
    }

    override suspend fun add(categoryDto: CategoryDto?): Long? =
        DBManager.postData(
            dataClass = CategoryTable::class,
            dataObject = mapper.toCategoryTable(categoryDto),
            tableName = CATEGORY_TABLE_NAME
        )


    override suspend fun update(categoryDto: CategoryDto?): Boolean{
         repository.updateData(
            dataClass = CategoryTable::class,
            dataObject = mapper.toCategoryTable(categoryDto),
            tableName = CATEGORY_TABLE_NAME,
            idColumn = "id"
        )
        return true
    }

    override suspend fun delete(id: Long?): Boolean =
        repository.deleteData(tableName = CATEGORY_TABLE_NAME, whereValue = id)

    override suspend fun getAll(): List<CategoryDto?> {
        TODO("Not yet implemented")
    }
}