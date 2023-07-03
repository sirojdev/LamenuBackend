package mimsoft.io.features.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.Language
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.*
import mimsoft.io.features.product.product_extra.ProductExtraService
import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.features.product.product_label.ProductLabelService
import mimsoft.io.features.product.product_option.ProductOptionService
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

    override suspend fun getAllProductInfo(merchantId: Long?): List<ProductInfoDto?> {
        val query = """
            select 
                p.id              p_id,
                p.name_uz         p_name_uz,
                p.name_ru         p_name_ru,
                p.name_eng        p_name_eng,
                p.description_uz  p_description_uz,
                p.description_ru  p_description_ru,
                p.description_eng p_description_eng,
                p.image           p_image,
                p.cost_price      p_cost_price,
                p.category_id,
                p.time_cooking_max,
                p.time_cooking_min,
                p.delivery_enabled,
                p.id_rkeeper,
                p.id_jowi,
                p.id_join_poster,
                p.active,
                COALESCE( pan.count, -1) pan_count, 
                c.id              c_id,
                c.name_uz         c_name_uz,
                c.name_ru         c_name_ru,
                c.name_eng        c_name_eng,
                c.image           c_image,
                c.bg_color        c_bg_color,
                c.text_color      c_text_color
            from product p
                left join category c on p.category_id = c.id
                left join pantry pan on pan.product_id = p.id
            where p.merchant_id = $merchantId and p.deleted = false""".trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<ProductInfoDto>()
                while (rs.next()) {
                    val dto = ProductInfoDto(
                        product = ProductDto(
                            id = rs.getLong("p_id"),
                            name = TextModel(
                                uz = rs.getString("p_name_uz"),
                                ru = rs.getString("p_name_ru"),
                                eng = rs.getString("p_name_eng")
                            ),
                            description = TextModel(
                                uz = rs.getString("p_description_uz"),
                                ru = rs.getString("p_description_ru"),
                                eng = rs.getString("p_description_eng")
                            ),
                            image = rs.getString("p_image"),
                            costPrice = rs.getLong("p_cost_price"),
                            categoryId = rs.getLong("category_id"),
                            timeCookingMax = rs.getLong("time_cooking_max"),
                            timeCookingMin = rs.getLong("time_cooking_min"),
                            deliveryEnabled = rs.getBoolean("delivery_enabled"),
                            productIntegration = ProductIntegrationDto(
                                idRkeeper = rs.getLong("id_rkeeper"),
                                idJowi = rs.getLong("id_jowi"),
                                idJoinPoster = rs.getLong("id_join_poster")
                            ),
                            active = rs.getBoolean("active"),
                            count = rs.getLong("pan_count"),
                            category = CategoryDto(
                                id = rs.getLong("c_id"),
                                name = TextModel(
                                    uz = rs.getString("c_name_uz"),
                                    ru = rs.getString("c_name_ru"),
                                    eng = rs.getString("c_name_eng"),
                                ),
                                image = rs.getString("c_image"),
                                bgColor = rs.getString("c_bg_color"),
                                textColor = rs.getString("c_text_color")
                            )
                        )
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }
    }

    override suspend fun getAll(merchantId: Long?): List<ProductDto?> {
        val query = """
        select p.id              p_id,
            p.name_uz         p_name_uz,
            p.name_ru         p_name_ru,
            p.name_eng        p_name_eng,
            p.description_uz  p_description_uz,
            p.description_ru  p_description_ru,
            p.description_eng p_description_eng,
            p.image           p_image,
            p.cost_price      p_cost_price,
            p.category_id,
            p.time_cooking_max,
            p.time_cooking_min,
            p.delivery_enabled,
            p.id_rkeeper,
            p.id_jowi,
            p.id_join_poster,
            p.active,
            COALESCE( pan.count, -1) pan_count 
            from product p
            left join pantry pan on pan.product_id = p.id
                where p.merchant_id = $merchantId and not p.deleted
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<ProductDto>()
                while (rs.next()) {
                    val dto = ProductDto(
                        id = rs.getLong("p_id"),
                        name = TextModel(
                            uz = rs.getString("p_name_uz"),
                            ru = rs.getString("p_name_ru"),
                            eng = rs.getString("p_name_eng")
                        ),
                        description = TextModel(
                            uz = rs.getString("p_description_uz"),
                            ru = rs.getString("p_description_ru"),
                            eng = rs.getString("p_description_eng")
                        ),
                        image = rs.getString("p_image"),
                        costPrice = rs.getLong("p_cost_price"),
                        categoryId = rs.getLong("category_id"),
                        timeCookingMax = rs.getLong("time_cooking_max"),
                        timeCookingMin = rs.getLong("time_cooking_min"),
                        deliveryEnabled = rs.getBoolean("delivery_enabled"),
                        productIntegration = ProductIntegrationDto(
                            idRkeeper = rs.getLong("id_rkeeper"),
                            idJowi = rs.getLong("id_jowi"),
                            idJoinPoster = rs.getLong("id_join_poster")
                        ),
                        active = rs.getBoolean("active"),
                        count = rs.getLong("pan_count")
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }
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

    override suspend fun getProductInfo(merchantId: Long?, id: Long?): ProductInfoDto? {
        val query = """
            select 
                p.id              p_id,
                p.name_uz         p_name_uz,
                p.name_ru         p_name_ru,
                p.name_eng        p_name_eng,
                p.description_uz  p_description_uz,
                p.description_ru  p_description_ru,
                p.description_eng p_description_eng,
                p.image           p_image,
                p.cost_price      p_cost_price,
                p.category_id,
                p.time_cooking_max,
                p.time_cooking_min,
                p.delivery_enabled,
                p.id_rkeeper,
                p.id_jowi,
                p.id_join_poster,
                p.active,
                c.id              c_id,
                c.name_uz         c_name_uz,
                c.name_ru         c_name_ru,
                c.name_eng        c_name_eng,
                c.image           c_image,
                c.bg_color        c_bg_color,
                c.text_color      c_text_color
            from product p
                left join category c on p.category_id = c.id 
                where p.deleted = false and p.merchant_id = $merchantId 
                    and p.id = $id
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext ProductInfoDto(
                        product = ProductDto(
                            id = rs.getLong("p_id"),
                            name = TextModel(
                                uz = rs.getString("p_name_uz"),
                                ru = rs.getString("p_name_ru"),
                                eng = rs.getString("p_name_eng")
                            ),
                            description = TextModel(
                                uz = rs.getString("p_description_uz"),
                                ru = rs.getString("p_description_ru"),
                                eng = rs.getString("p_description_eng")
                            ),
                            image = rs.getString("p_image"),
                            costPrice = rs.getLong("p_cost_price"),
                            categoryId = rs.getLong("category_id"),
                            timeCookingMax = rs.getLong("time_cooking_max"),
                            timeCookingMin = rs.getLong("time_cooking_min"),
                            deliveryEnabled = rs.getBoolean("delivery_enabled"),
                            productIntegration = ProductIntegrationDto(
                                idRkeeper = rs.getLong("id_rkeeper"),
                                idJowi = rs.getLong("id_jowi"),
                                idJoinPoster = rs.getLong("id_join_poster")
                            ),
                            active = rs.getBoolean("active"),
                            category = CategoryDto(
                                id = rs.getLong("category_id"),
                                name = TextModel(
                                    uz = rs.getString("c_name_uz"),
                                    ru = rs.getString("c_name_ru"),
                                    eng = rs.getString("c_name_eng"),
                                ),
                                image = rs.getString("c_image"),
                                bgColor = rs.getString("c_bg_color"),
                                textColor = rs.getString("c_text_color")
                            )
                        )
                    )
                } else return@withContext null
            }
        }
    }

    override suspend fun add(productTable: ProductTable?): Long? =
        DBManager.postData(dataClass = ProductTable::class, dataObject = productTable, tableName = PRODUCT_TABLE_NAME)

    override suspend fun update(dto: ProductDto?): Boolean {
        val merchantId = dto?.merchantId
        val query = "UPDATE $PRODUCT_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?, " +
                " description_uz = ?, " +
                " description_ru = ?," +
                " description_eng = ?," +
                " image = ? ," +
                " category_id = ${dto?.categoryId} ," +
                " cost_price = ${dto?.costPrice}," +
                " id_rkeeper = ${dto?.productIntegration?.idRkeeper}," +
                " id_join_poster = ${dto?.productIntegration?.idJoinPoster}," +
                " id_jowi = ${dto?.productIntegration?.idJowi}," +
                " delivery_enabled = ${dto?.deliveryEnabled}," +
                " time_cooking_max = ${dto?.timeCookingMax}," +
                " time_cooking_min = ${dto?.timeCookingMin}," +
                " updated = ?" +
                " WHERE id = ${dto?.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto?.name?.uz)
                    ti.setString(2, dto?.name?.ru)
                    ti.setString(3, dto?.name?.eng)
                    ti.setString(4, dto?.description?.uz)
                    ti.setString(5, dto?.description?.ru)
                    ti.setString(6, dto?.description?.eng)
                    ti.setString(7, dto?.image)
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
                return@withContext !rs
            }
        }
    }

    override suspend fun getProductInfo(id: Long, merchantId: Long?): ProductInfoDto? {
        val query = """
            select p.*, c.name_uz c_name_uz, c.name_ru c_name_ru, c.name_eng c_name_eng
                from product p
            inner join category c on p.category_id = c.id 
                where p.merchant_id = $merchantId 
                and p.id = $id 
                and p.deleted = false 
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext ProductInfoDto(
                        product = ProductDto(
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
                            category = CategoryDto(
                                name = TextModel(
                                    uz = rs.getString("c_name_uz"),
                                    ru = rs.getString("c_name_ru"),
                                    eng = rs.getString("c_name_eng")
                                )
                            ),
                            productIntegration = ProductIntegrationDto(
                                idJowi = rs.getLong("id_jowi"),
                                idRkeeper = rs.getLong("id_rkeeper"),
                                idJoinPoster = rs.getLong("id_join_poster")
                            ),
                            timeCookingMin = rs.getLong("time_cooking_min"),
                            timeCookingMax = rs.getLong("time_cooking_max"),
                            deliveryEnabled = rs.getBoolean("delivery_enabled")
                        ),
                        labels = ProductLabelService.getLabelsByProductId(id, merchantId = merchantId),
                        options = ProductOptionService.getOptionsByProductId(id, merchantId = merchantId),
                        extras = ProductExtraService.getExtrasByProductId(id, merchantId = merchantId)
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getAllByCategories(merchantId: Long?, categoryId: Long?): ArrayList<ProductDto> {
        val sql = "select * from $PRODUCT_TABLE_NAME where merchant_id = $merchantId  and category_id = $categoryId and  deleted = false"
        val listProduct = ArrayList<ProductDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(sql).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    val product = ProductDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng")
                        )
                    )
                    listProduct.add(product)
                }
            }
        }
        return listProduct;
    }

    suspend fun getByName(text: String, lang: Language, merchantId: Long): ProductDto? {
        val name: String = when (lang) {
            Language.UZ -> "name_uz"
            Language.RU -> "name_ru"
            else -> "name_eng"
        }
        val sql =
            "select * from $PRODUCT_TABLE_NAME where merchant_id = ${merchantId}  and   $name = ? and deleted = false and active = true"
        var product: ProductDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(sql).apply {
                    setString(1, text)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    product = ProductDto(
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

            }
        }
        return product
    }
}

