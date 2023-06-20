package mimsoft.io.features.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductMapper
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.enums.Language
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel
import java.sql.Timestamp


object ProductRepositoryImpl : ProductRepository {
    val repository: BaseRepository = DBManager
    val mapper = ProductMapper

    override suspend fun getAll(merchantId: Long?): List<ProductTable?> {
        val data = repository.getPageData(
            dataClass = ProductTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = PRODUCT_TABLE_NAME
        )?.data

        return data ?: emptyList()
    }

    override suspend fun get(id: Long?, merchantId: Long?): ProductTable? {
        val data = repository.getPageData(
            dataClass = ProductTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = PRODUCT_TABLE_NAME
        )?.data?.firstOrNull()
        return data
    }

    override suspend fun add(productTable: ProductTable?): Long? =
        DBManager.postData(dataClass = ProductTable::class, dataObject = productTable, tableName = PRODUCT_TABLE_NAME)

    override suspend fun update(dto: ProductDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $PRODUCT_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " description_uz = ?, " +
                " description_ru = ?," +
                " description_eng = ?," +
                " image = ? ," +
                " cost_price = ${dto.costPrice}," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name?.uz)
                    ti.setString(2, dto.name?.ru)
                    ti.setString(3, dto.name?.eng)
                    ti.setString(4, dto.description?.uz)
                    ti.setString(5, dto.description?.ru)
                    ti.setString(6, dto.description?.eng)
                    ti.setString(7, dto.image)
                    ti.setTimestamp(8, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $PRODUCT_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    fun getAllByCategories(merchantId: Long?, categoryId: Long?): ArrayList<ProductDto> {
        val connection = repository.connection()
        val sql =
            "select * from $PRODUCT_TABLE_NAME where merchant_id = $merchantId  and category_id = $categoryId and  deleted = false"
        val prSt = connection.prepareStatement(sql)
        val rs = prSt.executeQuery()
        val listProduct = ArrayList<ProductDto>()
        while (rs.next()) {
            var product: ProductDto = ProductDto(
                id = rs.getLong("id"),
                name = (TextModel(
                    uz = rs.getString("name_uz"),
                    ru = rs.getString("name_ru"),
                    eng = rs.getString("name_eng")
                )
                        )
            )
            listProduct.add(product)
        }
        return listProduct;

    }

    fun getByName(text: String, profile: BotUsersDto): ProductDto? {
        var name: String = when (profile.language) {
            Language.UZ -> "name_uz"
            Language.RU -> "name_ru"
            else -> "name_eng"
        }
        val connection = repository.connection()
        val sql =
            "select * from $PRODUCT_TABLE_NAME where merchant_id = ${profile.merchantId}  and   $name = ? and deleted = false and active = true"
        val prSt = connection.prepareStatement(sql)
        prSt.setString(1, text)
        val rs = prSt.executeQuery()
        if (rs.next()) {
            return ProductDto(
                id = rs.getLong("id"),
                name = (TextModel(
                    uz = rs.getString("name_uz"),
                    ru = rs.getString("name_ru"),
                    eng = rs.getString("name_eng")
                )),
                description = (
                        TextModel(
                            uz = rs.getString("description_uz"),
                            ru = rs.getString("description_ru"),
                            eng = rs.getString("description_eng")
                        )
                        ),
                image = rs.getString("image"),
                costPrice = rs.getLong("cost_price")
            )
        }
        return null
    }
}