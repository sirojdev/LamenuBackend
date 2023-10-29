package mimsoft.io.features.category_group

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryMapper
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.product.ProductDto
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

        return data?.map { mapper.toDto(it ?: CategoryGroupTable()) } ?: emptyList()
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
                " updated = ?, " +
                " priority = ${dto.priority}" +
                " WHERE id = ${dto.id} and merchant_id = ${dto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.title?.uz)
                    ti.setString(2, dto.title?.ru)
                    ti.setString(3, dto.title?.eng)
                    ti.setString(4, dto.bgColor)
                    ti.setTimestamp(5, Timestamp(System.currentTimeMillis()))
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
                it.prepareStatement(query).execute()
            }
        }
        return true
    }

    suspend fun getById(merchantId: Long?, id: Long?): CategoryGroupDto? {
        val query =
            "select * from $CATEGORY_GROUP_TABLE where merchant_id = $merchantId and id = $id and deleted = false order by priority, created"
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

    suspend fun getClient(merchantId: Long?): List<CategoryGroupDto> {
        val query = """
            SELECT cg.id,
                   cg.title_uz,
                   cg.title_ru,
                   cg.title_eng,
                   cg.merchant_id,
                   cg.priority,
                   cg.bg_color,
                   (SELECT json_agg(json_build_object(
                           'id', c.id,
                           'nameUz', c.name_uz,
                           'nameRu', c.name_ru,
                           'nameEng', c.name_eng,
                           'image', c.image,
                           'priority', c.priority,
                           'groupId', c.group_id
                       ))
                    FROM category c
                    WHERE c.group_id = cg.id
                      AND c.merchant_id = $merchantId 
                      and not c.deleted) AS categories 
            FROM category_group cg
            WHERE cg.merchant_id = $merchantId
              and cg.deleted = false
            order by priority, created
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                val data = arrayListOf<CategoryGroupDto>()
                while (rs.next()) {
                    val categories = rs?.getString("categories")
                    val typeToken = object : TypeToken<List<CategoryTable>>() {}.type
                    val list = gson.fromJson<List<CategoryTable>?>(categories, typeToken) ?: emptyList()
                    val a = CategoryGroupDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        bgColor = rs.getString("bg_color"),
                        categories = list.map { CategoryMapper.toCategoryDto(it)!! },
                        priority = rs.getInt("priority")
                    )
                    data.add(a)
                }
                return@withContext data
            }
        }
    }
    suspend fun getCategoryGroupForTGBot(merchantId: Long?): List<CategoryGroupDto> {
        val query = """
            SELECT cg.id,
                   cg.title_uz,
                   cg.title_ru,
                   cg.title_eng,
                   cg.merchant_id,
                   cg.priority
            FROM category_group cg
            WHERE cg.merchant_id = $merchantId
              and cg.deleted = false
            order by priority, created
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val data = arrayListOf<CategoryGroupDto>()
                while (rs.next()) {
                    val a = CategoryGroupDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        priority = rs.getInt("priority")
                    )
                    data.add(a)
                }
                return@withContext data
            }
        }
    }


    suspend fun getCategoryGroupWithBranchId(merchantId: Long?, branchId: Long?): List<CategoryGroupDto> {
        val query = """
            SELECT cg.id,
                   cg.title_uz,
                   cg.title_ru,
                   cg.title_eng,
                   cg.merchant_id,
                   cg.priority,
                   cg.bg_color,
                   (SELECT json_agg(json_build_object(
                           'id', c.id,
                           'nameUz', c.name_uz,
                           'nameRu', c.name_ru,
                           'nameEng', c.name_eng,
                           'image', c.image,
                           'priority', c.priority,
                           'groupId', c.group_id
                       ))
                    FROM category c
                    WHERE c.group_id = cg.id
                      AND c.merchant_id = $merchantId 
                      and not c.deleted) AS categories 
            FROM category_group cg
            WHERE cg.merchant_id = $merchantId
              and branch_id = $branchId 
              and cg.deleted = false
            order by priority, created
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                val data = arrayListOf<CategoryGroupDto>()
                while (rs.next()) {
                    val categories = rs?.getString("categories")
                    val typeToken = object : TypeToken<List<CategoryTable>>() {}.type
                    val list = gson.fromJson<List<CategoryTable>?>(categories, typeToken) ?: emptyList()
                    val a = CategoryGroupDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        bgColor = rs.getString("bg_color"),
                        categories = list.map { CategoryMapper.toCategoryDto(it)!! },
                        priority = rs.getInt("priority")
                    )
                    data.add(a)
                }
                return@withContext data
            }
        }
    }


    suspend fun getCategoryGroupById(merchantId: Long?, id: Long?): CategoryGroupDto? {
        val query = """
       SELECT cg.id,
       cg.bg_color,
       cg.title_uz,
       cg.title_ru,
       cg.title_eng,
       cg.merchant_id,
       cg.priority,
       c_list
FROM category_group cg
         left join (SELECT json_agg(json_build_object(
        'id', c.id,
        'nameUz', c.name_uz,
        'nameRu', c.name_ru,
        'nameEng', c.name_eng,
        'image', c.image,
        'priority', c.priority
    )) as c_list,
                           c.group_id
                    FROM (select * from category c2 where c2.merchant_id = 
                    $merchantId order by c2.priority) as c
                    where c.merchant_id = $merchantId
                    group by c.group_id) c on c.group_id = cg.id
WHERE cg.id = $id
  and cg.merchant_id = $merchantId
  and cg.deleted = false
order by cg.priority,
         cg.created
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val categoryList = arrayListOf<CategoryDto>()
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                if (rs.next()) {
                    val categories = rs?.getString("c_list")
                    val typeToken = object : TypeToken<List<CategoryTable>>() {}.type
                    val list = gson.fromJson<List<CategoryTable?>?>(categories, typeToken) ?: emptyList()
                    val dtoList = list.map { CategoryMapper.toCategoryDto(it) }
                    dtoList.map {
                        val list1 = arrayListOf<ProductDto>()
                        val prod =
                            ProductRepositoryImpl.getAllByCategories(merchantId = merchantId, categoryId = it?.id)
                        prod.map {
                            list1.add(
                                it.copy(
                                    options = OptionRepositoryImpl.getOptionsByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    ),
                                    extras = ExtraRepositoryImpl.getExtrasByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    ),
                                    labels = ProductLabelService.getLabelsByProductId(
                                        merchantId = merchantId,
                                        productId = it.id
                                    )
                                )
                            )
                        }
                        it?.copy(products = list1)?.let { it1 ->
                            categoryList.add(
                                it1
                            )
                        }
                    }
                    return@withContext CategoryGroupDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        categories = categoryList,
                        bgColor = rs.getString("bg_color"),
                        priority = rs.getInt("priority")
                    )
                }
                return@withContext null
            }
        }
    }

    suspend fun getCategoryGroupByIdInBranch(branchId: Long?, groupId: Long?): MutableList<CategoryDto> {
        val query = """
       select * from category c inner join category_group cg on c.group_id = cg.id
        where c.group_id = $groupId and cg.branch_id = $branchId and c.deleted = false and cg.deleted = false 
        """.trimIndent()
        val list = mutableListOf<CategoryDto>()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        CategoryDto(
                            id = rs.getLong("id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            image = rs.getString("image"),
                            priority = rs.getInt("priority")
                        )
                    )
                }
            }
        }
        return list
    }
}
