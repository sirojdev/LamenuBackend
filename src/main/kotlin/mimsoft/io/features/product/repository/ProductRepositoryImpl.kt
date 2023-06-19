package mimsoft.io.features.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductMapper
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
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
        val filter = mutableMapOf<String, Any>()
        val some = if (merchantId == null) {
            filter["id"] = Long
            mapOf("id" to id as Any)
        } else {
            filter["id"] = Long
            filter["merchant_id"] = merchantId
            mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            )
        }
        val data = repository.getPageData(
            dataClass = ProductTable::class,
            where = some,
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
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).execute()
                return@withContext rs
            }
        }
    }
}