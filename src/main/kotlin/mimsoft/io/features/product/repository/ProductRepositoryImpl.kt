package mimsoft.io.features.product.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.product.*
import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.features.product.product_label.ProductLabelService
import mimsoft.io.features.telegram_bot.Language
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
                p.discount,
                COALESCE( pan.count, -1) pan_count, 
                c.id              c_id,
                c.name_uz         c_name_uz,
                c.name_ru         c_name_ru,
                c.name_eng        c_name_eng,
                c.image           c_image
            from product p
                left join category c on p.category_id = c.id
                left join pantry pan on pan.product_id = p.id
            where p.merchant_id = $merchantId
             and p.deleted = false""".trimIndent()
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
                            timeCookingMax = rs.getLong("time_cooking_max"),
                            timeCookingMin = rs.getLong("time_cooking_min"),
                            deliveryEnabled = rs.getBoolean("delivery_enabled"),
                            discount = rs.getLong("discount"),
                            productIntegration = ProductIntegrationDto(
                                idRkeeper = rs.getLong("id_rkeeper"),
                                idJowi = rs.getString("id_jowi"),
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
                            )
                        )
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }
    }

    override suspend fun getAll(merchantId: Long?, search: String?): List<ProductDto?> {
        var query = """
        select p.id           p_id,
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
            p.discount,
            COALESCE( pan.count, -1) pan_count 
            from product p
            left join pantry pan on pan.product_id = p.id
                where p.merchant_id = $merchantId and not p.deleted 
        """.trimIndent()
        if (search != null) {
            val s = search.lowercase().replace("'", "_")
            query += """
                and (
                lower(p.name_uz) like '%$s%'  or 
                lower(p.name_ru) like '%$s%'  or 
                lower(p.name_eng) like '%$s%' or 
                lower(p.description_uz) like '%$s%' or 
                lower(p.description_ru) like '%$s%' or 
                lower(p.description_eng) like '%$s%')
            """

        }
        println(search)
        println(query)
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
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
                        category = CategoryDto(id = rs.getLong("category_id")),
                        timeCookingMax = rs.getLong("time_cooking_max"),
                        timeCookingMin = rs.getLong("time_cooking_min"),
                        deliveryEnabled = rs.getBoolean("delivery_enabled"),
                        discount = rs.getLong("discount"),
                        productIntegration = ProductIntegrationDto(
                            idRkeeper = rs.getLong("id_rkeeper"),
                            idJowi = rs.getString("id_jowi"),
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

    /*    override suspend fun getProductInfo(merchantId: Long?, id: Long?): ProductInfoDto? {
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
                    println("Query -> $query")
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
        }*/

    override suspend fun add(productTable: ProductTable?): Long? =
        DBManager.postData(dataClass = ProductTable::class, dataObject = productTable, tableName = PRODUCT_TABLE_NAME)

    override suspend fun update(dto: ProductDto?): Boolean {
        var rs: Int
        val query = """
               update product
               set name_uz          = ?,
                   name_ru          = ?,
                   name_eng         = ?,
                   description_uz   = ?,
                   description_ru   = ?,
                   description_eng  = ?,
                   image            = ?,
                   category_id      = ${dto?.category?.id},
                   cost_price       = ${dto?.costPrice},
                   id_rkeeper       = ${dto?.productIntegration?.idRkeeper},
                   id_join_poster   = ${dto?.productIntegration?.idJoinPoster},
                   id_jowi          = ?,
                   delivery_enabled = ${dto?.deliveryEnabled},
                   time_cooking_max = ${dto?.timeCookingMax},
                   time_cooking_min = ${dto?.timeCookingMin},
                   discount         = ${dto?.discount},
                   updated          = ?
                   where id = ${dto?.id}
                   and merchant_id = ${dto?.merchantId}
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.setString(1, dto?.name?.uz)
                    this.setString(2, dto?.name?.ru)
                    this.setString(3, dto?.name?.eng)
                    this.setString(4, dto?.description?.uz)
                    this.setString(5, dto?.description?.ru)
                    this.setString(6, dto?.description?.eng)
                    this.setString(7, dto?.image)
                    this.setLong(8, dto?.discount!!)
                    this.setTimestamp(9, Timestamp(System.currentTimeMillis()))
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var rs: Int
        val query = """
            update product
            set deleted = true
            where merchant_id = $merchantId
             and id = $id
             and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }

    override suspend fun getProductInfo(merchantId: Long?, id: Long?): ProductInfoDto? {
        val query = """
            select p.*, c.id c_id, c.name_uz c_name_uz, c.name_ru c_name_ru, c.name_eng c_name_eng, c.image c_image,  c.group_id c_group_id
                from product p
            inner join category c on p.category_id = c.id 
                where p.merchant_id = $merchantId 
                and p.id = $id 
                and not p.deleted 
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
                                id = rs.getLong("c_id"),
                                name = TextModel(
                                    uz = rs.getString("c_name_uz"),
                                    ru = rs.getString("c_name_ru"),
                                    eng = rs.getString("c_name_eng")
                                ),
                                image = rs.getString("c_image"),
                                groupId = rs.getLong("c_group_id"),
                            ),
                            productIntegration = ProductIntegrationDto(
                                idJowi = rs.getString("id_jowi"),
                                idRkeeper = rs.getLong("id_rkeeper"),
                                idJoinPoster = rs.getLong("id_join_poster")
                            ),
                            timeCookingMin = rs.getLong("time_cooking_min"),
                            timeCookingMax = rs.getLong("time_cooking_max"),
                            deliveryEnabled = rs.getBoolean("delivery_enabled"),
                            discount = rs.getLong("discount")
                        ),
                        labels = ProductLabelService.getLabelsByProductId(id, merchantId = merchantId),
                        options = OptionRepositoryImpl.getOptionsByProductId(merchantId = merchantId, productId = id),
                        extras = ExtraRepositoryImpl.getExtrasByProductId(merchantId = merchantId, productId = id)
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getAllByCategories(merchantId: Long?, categoryId: Long?): List<ProductDto> {
        val sql =
            "select * from $PRODUCT_TABLE_NAME where merchant_id = $merchantId " +
                    "and category_id = $categoryId and  deleted = false"
        val listProduct = arrayListOf<ProductDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use { c ->
                val rs = c.prepareStatement(sql).executeQuery()
                while (rs.next()) {
                    val product = ProductDto(
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
                        productIntegration = ProductIntegrationDto(
                            idJowi = rs.getString("id_jowi"),
                            idRkeeper = rs.getLong("id_rkeeper"),
                            idJoinPoster = rs.getLong("id_join_poster"),
                        ),
                        timeCookingMin = rs.getLong("time_cooking_min"),
                        timeCookingMax = rs.getLong("time_cooking_max"),
                        deliveryEnabled = rs.getBoolean("delivery_enabled"),
                        discount = rs.getLong("discount")
                    )
                    listProduct.add(product)
                }
            }
        }

        return listProduct
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
                        costPrice = rs.getLong("cost_price"),
                        discount = rs.getLong("discount")
                    )
                }
            }
        }
        return product
    }
}
