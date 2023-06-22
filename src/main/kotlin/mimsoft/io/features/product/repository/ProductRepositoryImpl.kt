package mimsoft.io.features.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.product.*
import mimsoft.io.features.product.product_extra.ProductExtraService
import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.features.product.product_label.ProductLabelService
import mimsoft.io.features.product.product_option.ProductOptionService
import mimsoft.io.features.staff.StaffService
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
                " category_id = ${dto.categoryId} ," +
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

    override suspend fun getProductInfo(id: Long, merchantId: Long?): ProductInfoResponseDto? {
        val query = """
            select p.*, c.name_uz c_name_uz, c.name_ru c_name_ru, c.name_eng c_name_eng,
                p_i.id_jowi p_i_id_jowi, p_i.id_rkeeper p_i_id_rkeeper, p_i.id_join_poster p_i_id_join_poster
                from product p
            inner join category c on p.category_id = c.id
            inner join product_integration p_i on p.product_integration_id = p_i.id
                where p.merchant_id = $merchantId 
                and p.id = $id 
                and p.deleted = false
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext ProductInfoResponseDto(
                        product = ProductResponseDto(
                            id = rs.getLong("id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            description = TextModel(
                                uz = rs.getString("description_uz"),
                                ru = rs.getString("description_ru"),
                                eng = rs.getString("description_eng")
                            ),
                            image = rs.getString("image"),
                            costPrice = rs.getLong("cost_price"),
                            active = rs.getBoolean("active"),
                            categoryName = TextModel(
                                uz = rs.getString("c_name_uz"),
                                ru = rs.getString("c_name_ru"),
                                eng = rs.getString("c_name_eng")
                            ),
                            productIntegration = ProductIntegrationDto(
                                idJowi = rs.getBoolean("p_i_id_jowi"),
                                idRkeeper = rs.getBoolean("p_i_id_rkeeper"),
                                idJoinPoster = rs.getBoolean("p_i_id_join_poster")
                            ),
                            timeCookingMin = rs.getLong("time_min"),
                            timeCookingMax = rs.getLong("time_max"),
                            deliveryEnabled = rs.getBoolean("enabled")
                        ),
                        labels = ProductLabelService.getLabelsByProductId(id, merchantId = merchantId),
                        options = ProductOptionService.getOptionsByProductId(id, merchantId = merchantId),
                        extras = ProductExtraService.getExtrasByProductId(id, merchantId = merchantId)
                    )
                } else return@withContext null
            }
        }
    }
}