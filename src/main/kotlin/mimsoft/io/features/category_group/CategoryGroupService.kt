package mimsoft.io.features.category_group

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CategoryMapper
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.category.ClientCategoryDto
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.product.ClientProductDto
import mimsoft.io.features.product.product_label.ProductLabelService
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

object CategoryGroupService {
    val repository: BaseRepository = DBManager
    val mapper = CategoryGroupMapper
    suspend fun getAll(merchantId: Long?): List<CategoryGroupDto> {
        val data = repository.getPageData(
            dataClass = CategoryGroupTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = CATEGORY_GROUP_TABLE
        )?.data

        return data?.map { mapper.toDto(it) } ?: emptyList()
    }

    suspend fun add(dto: CategoryGroupDto): Long? =
        DBManager.postData(
            dataClass = CategoryGroupTable::class,
            dataObject = mapper.toTable(dto),
            tableName = CATEGORY_GROUP_TABLE
        )

    suspend fun update(dto: CategoryGroupDto): Boolean {
        val query = "UPDATE $CATEGORY_GROUP_TABLE " +
                "SET" +
                " title_uz = ?, " +
                " title_ru = ?," +
                " title_eng = ?," +
                " bg_color = ?," +
                " text_color = ?," +
                " updated = ?" +
                " priority = ${dto.priority}" +
                " WHERE id = ${dto.id} and merchant_id = ${dto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.title?.uz)
                    ti.setString(2, dto.title?.ru)
                    ti.setString(3, dto.title?.eng)
                    ti.setString(4, dto.bgColor)
                    ti.setString(5, dto.textColor)
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $CATEGORY_GROUP_TABLE set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun getById(merchantId: Long?, id: Long?): CategoryGroupDto? {
        val query =
            "select * from $CATEGORY_GROUP_TABLE where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        CategoryGroupTable(
                            id = rs.getLong("id"),
                            titleUz = rs.getString("title_uz"),
                            titleRu = rs.getString("title_ru"),
                            titleEng = rs.getString("title_eng"),
                            bgColor = rs.getString("bg_color"),
                            merchantId = rs.getLong("merchant_id"),
                            priority = rs.getInt("priority")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getClient(merchantId: Long?): List<CategoryGroupClientDto> {
        val query = """
            SELECT cg.id,
       cg.bg_color,
       cg.title_uz,
       cg.title_ru,
       cg.title_eng,
       cg.merchant_id,
       cg.text_color,
       cg.priority,
       (SELECT json_agg(json_build_object(
               'id', c.id,
               'bgColor', c.bg_color,
               'nameUz', c.name_uz,
               'nameRu', c.name_ru,
               'nameEng', c.name_eng,
               'image', c.image,
               'textColor', c.text_color,
               'priority', c.priority,
               'groupId', c.group_id
           ))
        FROM category c
        WHERE c.group_id = cg.id
          AND c.merchant_id = $merchantId and not c.deleted) AS categories 
    FROM category_group cg 
    WHERE cg.merchant_id = $merchantId 
        and cg.deleted = false 
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
//                val categoryList = arrayListOf<ClientCategoryDto>()
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                val data = arrayListOf<CategoryGroupClientDto>()
                while (rs.next()) {
                    val categories = rs?.getString("categories")
                    val typeToken = object : TypeToken<List<CategoryTable>>() {}.type
                    val list = gson.fromJson<List<CategoryTable?>?>(categories, typeToken) ?: emptyList()
//                    val dtoList = list.map { CategoryMapper.toCategoryDto(it) }
//                    dtoList.map {
//                        val list1 = arrayListOf<ClientProductDto>()
//                        val prod =
//                            ProductRepositoryImpl.getAllByCategories(merchantId = merchantId, categoryId = it?.id)
//                        prod.map {
//                            list1.add(
//                                ClientProductDto(
//                                    productDto = it,
//                                    option = OptionRepositoryImpl.getOptionsByProductId(
//                                        merchantId = merchantId,
//                                        productId = it.id
//                                    ),
//                                    extra = ExtraRepositoryImpl.getExtrasByProductId(
//                                        merchantId = merchantId,
//                                        productId = it.id
//                                    ),
//                                    label = ProductLabelService.getLabelsByProductId(
//                                        merchantId = merchantId,
//                                        productId = it.id
//                                    )
//                                )
//                            )
//                        }
//                        categoryList.add(
//                            ClientCategoryDto(
//                                categoryDto = it,
//                                clientProductDto = list1
//                            )
//                        )
//                    }
                    val a = CategoryGroupClientDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        categories = list.map { CategoryMapper.toCategoryDto(it)!! },
                        bgColor = rs.getString("bg_color"),
                        priority = rs.getInt("priority")
                    )
                    data.add(a)
                }
                return@withContext data
            }
        }
    }


    suspend fun getCategoryGroupById(merchantId: Long?, id: Long?): CategoryGroupClientDto? {
        val query = """
            SELECT cg.id,
       cg.bg_color,
       cg.title_uz,
       cg.title_ru,
       cg.title_eng,
       cg.merchant_id,
       cg.text_color,
       cg.priority,
       (SELECT json_agg(json_build_object(
               'id', c.id,
               'nameUz', c.name_uz,
               'nameRu', c.name_ru,
               'nameEng', c.name_eng,
               'image', c.image,
               'textColor', c.text_color,
               'bgColor', c.bg_color,
               'priority', c.priority
           ))
          FROM category c
          WHERE c.group_id = cg.id
          AND c.merchant_id = $merchantId) AS categories
      FROM category_group cg
      WHERE cg.id = $id and cg.merchant_id = $merchantId
      and cg.deleted = false order by  priority, created 
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val categoryList = arrayListOf<ClientCategoryDto>()
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                if (rs.next()) {
                    val categories = rs?.getString("categories")
                    val typeToken = object : TypeToken<List<CategoryTable>>() {}.type
                    val list = gson.fromJson<List<CategoryTable?>?>(categories, typeToken) ?: emptyList()
                    val dtoList = list.map { CategoryMapper.toCategoryDto(it) }
                    dtoList.map {
                        val list1 = arrayListOf<ClientProductDto>()
                        val prod =
                            ProductRepositoryImpl.getAllByCategories(merchantId = merchantId, categoryId = it?.id)
                        prod.map {
                            list1.add(
                                ClientProductDto(
                                    productDto = it,
                                    option = OptionRepositoryImpl.getOptionsByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    ),
                                    extra = ExtraRepositoryImpl.getExtrasByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    ),
                                    label = ProductLabelService.getLabelsByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    )
                                )
                            )
                        }
                        categoryList.add(
                            ClientCategoryDto(
                                categoryDto = it,
                                clientProductDto = list1
                            )
                        )
                    }
                    return@withContext CategoryGroupClientDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        categoriesWithProduct = categoryList,
                        bgColor = rs.getString("bg_color"),
                        priority = rs.getInt("priority")
                    )
                }
                return@withContext null
            }
        }
    }
}