package mimsoft.io.features.category.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryMapper
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object CategoryRepositoryImpl : CategoryRepository {

    val repository: BaseRepository = DBManager
    val mapper = CategoryMapper

    override suspend fun getAll(merchantId: Long?): List<CategoryDto?> {
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

    override suspend fun update(dto: CategoryDto): Boolean{
        val merchantId = dto.merchantId
        val query = "UPDATE $CATEGORY_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " image = ?, " +
                " bg_color = ?," +
                " text_color = ?," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name?.uz)
                    ti.setString(2, dto.name?.ru)
                    ti.setString(3, dto.name?.eng)
                    ti.setString(4, dto.image)
                    ti.setString(5, dto.bgColor)
                    ti.setString(6, dto.textColor)
                    ti.setTimestamp(7, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $CATEGORY_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}