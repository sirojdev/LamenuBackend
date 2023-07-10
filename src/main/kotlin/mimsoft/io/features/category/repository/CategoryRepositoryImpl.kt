package mimsoft.io.features.category.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryMapper
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.lamenu_bot.enums.Language
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


    override suspend fun update(dto: CategoryDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $CATEGORY_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " image = ?, " +
                " bg_color = ?," +
                " text_color = ?," +
                " updated = ? \n" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use { c ->
                c.prepareStatement(query).apply {
                    this.setString(1, dto.name?.uz)
                    this.setString(2, dto.name?.ru)
                    this.setString(3, dto.name?.eng)
                    this.setString(4, dto.image)
                    this.setString(5, dto.bgColor)
                    this.setString(6, dto.textColor)
                    this.setTimestamp(7, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $CATEGORY_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { it ->
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    override suspend fun getCategoryByName(merchantId: Long?, lang: Language, text: String?): CategoryDto? {
        if (merchantId == null) {
            return null
        }
        val name: String = when (lang) {
            Language.UZ -> "name_uz"
            Language.RU -> "name_ru"
            else -> "name_eng"
        }

        val query =
            "select * from $CATEGORY_TABLE_NAME where merchant_id = ? and deleted = false and $name = ? "
        var dto: CategoryDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setLong(1, merchantId!!)
                    setString(2, text)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    dto = mapper.toCategoryDto(
                        CategoryTable(
                            id = rs.getLong("id"),
                            nameUz = rs.getString("name_uz"),
                            nameRu = rs.getString("name_ru"),
                            nameEng = rs.getString("name_eng"),
                            merchantId = rs.getLong("merchant_id")
                        )
                    )
                } else null
            }
        }
        return dto
    }
}
