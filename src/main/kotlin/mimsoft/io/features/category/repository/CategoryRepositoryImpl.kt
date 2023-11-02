package mimsoft.io.features.category.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.*
import mimsoft.io.features.category_group.CategoryGroupDto
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductInfoDto
import mimsoft.io.features.product.product_label.ProductLabelService
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.telegram_bot.Language
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel

object CategoryRepositoryImpl : CategoryRepository {

  val repository: BaseRepository = DBManager
  val mapper = CategoryMapper

  override suspend fun getAllByClient(merchantId: Long?): List<ClientCategoryDto?> {
    val query =
      """
            SELECT c.id c_id,
                c.name_uz,
                c.name_ru,
                c.name_eng,
                c.image,
                c.priority c_priority,
                cg.id cg_id, 
                cg.merchant_id cg_merchant_id,
                cg.title_uz cg_title_uz,
                cg.title_ru cg_title_ru,
                cg.title_eng cg_title_eng,
                cg.bg_color cg_bg_color,
                cg.priority cg_priority,
                   (SELECT json_agg(json_build_object(
                       'id', p.id,
                       'nameUz', p.name_uz,
                       'nameRu', p.name_ru,
                       'nameEng', p.name_eng,
                       'descriptionUz', p.description_uz,
                       'descriptionRu', p.description_ru,
                       'descriptionEng', p.description_eng,
                       'image', p.image,
                       'costPrice', p.cost_price,
                       'timeCookingMin', p.time_cooking_min,
                       'timeCookingMax', p.time_cooking_max,
                       'deliveryEnabled', p.delivery_enabled,
                       'idRkeeper', p.id_rkeeper,
                       'idJoinPoster', p.id_join_poster,
                       'idJowi', p.id_jowi,
                       'active', p.active))
                FROM product p
                WHERE p.category_id = c.id
                AND p.merchant_id = $merchantId and not p.deleted) AS products 
            FROM category c left join category_group cg on c.group_id = cg.id 
            WHERE c.merchant_id = $merchantId 
            and c.deleted = false order by c.priority, c.created
        """
        .trimIndent()
    println("\ncategory for client $query")
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val gson = Gson()
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<ClientCategoryDto>()
        val list1 = arrayListOf<ProductInfoDto>()
        while (rs.next()) {
          val product = rs.getString("products")
          val typeToken = object : TypeToken<List<ProductDto>>() {}.type
          val products = gson.fromJson<List<ProductDto>>(product, typeToken)
          products?.map {
            list1.add(
              ProductInfoDto(
                product = it,
                options =
                  OptionRepositoryImpl.getOptionsByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  ),
                extras =
                  ExtraRepositoryImpl.getExtrasByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  ),
                labels =
                  ProductLabelService.getLabelsByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  )
              )
            )
          }
          val clientCategory =
            ClientCategoryDto(
              categoryDto =
                CategoryDto(
                  id = rs.getLong("c_id"),
                  name =
                    TextModel(
                      uz = rs.getString("name_uz"),
                      ru = rs.getString("name_ru"),
                      eng = rs.getString("name_eng"),
                    ),
                  image = rs.getString("image"),
                  priority = rs.getInt("c_priority")
                ),
              clientProductDto = list1,
              categoryGroup =
                CategoryGroupDto(
                  id = rs.getLong("cg_id"),
                  merchantId = rs.getLong("cg_merchant_id"),
                  title =
                    TextModel(
                      uz = rs.getString("cg_title_uz"),
                      ru = rs.getString("cg_title_ru"),
                      eng = rs.getString("cg_title_eng")
                    ),
                  bgColor = rs.getString("cg_bg_color"),
                  priority = rs.getInt("cg_priority")
                )
            )
          list.add(clientCategory)
        }
        return@withContext list
      }
    }
  }

  override suspend fun getCategoryForClientById(id: Long?, merchantId: Long?): ClientCategoryDto? {
    val query =
      """
            SELECT c.id c_id,
                c.name_uz,
                c.name_ru,
                c.name_eng,
                c.image,
                c.priority c_priority,
                cg.id cg_id, 
                cg.merchant_id cg_merchant_id,
                cg.title_uz cg_title_uz,
                cg.title_ru cg_title_ru,
                cg.title_eng cg_title_eng,
                cg.bg_color cg_bg_color,
                cg.priority cg_priority,
                   (SELECT json_agg(json_build_object(
                       'id', p.id,
                       'nameUz', p.name_uz,
                       'nameRu', p.name_ru,
                       'nameEng', p.name_eng,
                       'descriptionUz', p.description_uz,
                       'descriptionRu', p.description_ru,
                       'descriptionEng', p.description_eng,
                       'image', p.image,
                       'costPrice', p.cost_price,
                       'timeCookingMin', p.time_cooking_min,
                       'timeCookingMax', p.time_cooking_max,
                       'deliveryEnabled', p.delivery_enabled,
                       'idRkeeper', p.id_rkeeper,
                       'idJoinPoster', p.id_join_poster,
                       'idJowi', p.id_jowi,
                       'active', p.active))
                FROM product p
                WHERE p.category_id = c.id
                AND p.merchant_id = $merchantId and not p.deleted) AS products 
            FROM category c left join category_group cg on c.group_id = cg.id 
            WHERE c.id = $id and c.merchant_id = $merchantId 
            and c.deleted = false order by c.priority, c.created
        """
        .trimIndent()
    println("\ncategory for client $query")
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val gson = Gson()
        val rs = it.prepareStatement(query).executeQuery()
        val list1 = arrayListOf<ProductInfoDto>()
        if (rs.next()) {
          val product = rs.getString("products")
          val typeToken = object : TypeToken<List<ProductDto>>() {}.type
          val products = gson.fromJson<List<ProductDto>>(product, typeToken)
          products?.map {
            list1.add(
              ProductInfoDto(
                product = it,
                options =
                  OptionRepositoryImpl.getOptionsByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  ),
                extras =
                  ExtraRepositoryImpl.getExtrasByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  ),
                labels =
                  ProductLabelService.getLabelsByProductId(
                    merchantId = merchantId,
                    productId = it.id
                  )
              )
            )
          }
          return@withContext ClientCategoryDto(
            categoryDto =
              CategoryDto(
                id = rs.getLong("c_id"),
                name =
                  TextModel(
                    uz = rs.getString("name_uz"),
                    ru = rs.getString("name_ru"),
                    eng = rs.getString("name_eng"),
                  ),
                image = rs.getString("image"),
                priority = rs.getInt("c_priority")
              ),
            clientProductDto = list1,
            categoryGroup =
              CategoryGroupDto(
                id = rs.getLong("cg_id"),
                merchantId = rs.getLong("cg_merchant_id"),
                title =
                  TextModel(
                    uz = rs.getString("cg_title_uz"),
                    ru = rs.getString("cg_title_ru"),
                    eng = rs.getString("cg_title_eng")
                  ),
                bgColor = rs.getString("cg_bg_color"),
                priority = rs.getInt("cg_priority")
              )
          )
        }
        return@withContext null
      }
    }
  }

  override suspend fun getCategoryByCategoryGroupName(
    merchantId: Long?,
    categoryGroupName: String?,
    lang: Language
  ): List<CategoryDto>? {
    val name: String =
      when (lang) {
        Language.EN -> "title_eng"
        Language.RU -> "title_ru"
        else -> "title_uz"
      }

    val query =
      "select * from $CATEGORY_TABLE_NAME c inner join category_group cg on cg.id = c.group_id  where c.merchant_id = $merchantId and c.deleted = false and cg.$name = ? and cg.deleted = false order by c.priority, c.created"
    val list = ArrayList<CategoryDto>()
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              setString(1, categoryGroupName)
              this.closeOnCompletion()
            }
            .executeQuery()
        while (rs.next()) {
          list.add(
            CategoryDto(
              id = rs.getLong("id"),
              name =
                TextModel(
                  uz = rs.getString("name_uz"),
                  ru = rs.getString("name_ru"),
                  eng = rs.getString("name_eng"),
                ),
              merchantId = rs.getLong("merchant_id"),
              groupId = rs.getLong("group_id")
            )
          )
        }
      }
    }
    return list
  }

  override suspend fun getAll(merchantId: Long?): List<CategoryDto?> {
    val data =
      repository
        .getPageData(
          dataClass = CategoryTable::class,
          where = mapOf("merchant_id" to merchantId as Any),
          tableName = CATEGORY_TABLE_NAME
        )
        ?.data

    return data?.map { mapper.toCategoryDto(it) } ?: emptyList()
  }

  override suspend fun get(id: Long?, merchantId: Long?): CategoryDto? {
    val data =
      repository
        .getPageData(
          dataClass = CategoryTable::class,
          where = mapOf("merchant_id" to merchantId as Any, "id" to id as Any),
          tableName = CATEGORY_TABLE_NAME
        )
        ?.data
        ?.firstOrNull()
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
    val query =
      "UPDATE $CATEGORY_TABLE_NAME c" +
        "SET" +
        " coalesce(?, c.name_uz), " +
        " coalesce(?, c.name_ru)," +
        " coalesce(?, c.name_eng)," +
        " coalesce(?, c.image)," +
        " priority = coalesce(${dto.priority}, c.priority)," +
        " group_id = coalesce(${dto.groupId}, c.group_id)," +
        " updated = ? \n" +
        " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

    return withContext(DBManager.databaseDispatcher) {
      StaffService.repository.connection().use {
        return@withContext it
          .prepareStatement(query)
          .apply {
            this.setString(1, dto.name?.uz)
            this.setString(2, dto.name?.ru)
            this.setString(3, dto.name?.eng)
            this.setString(4, dto.image)
            this.setTimestamp(5, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .execute()
      }
    }
  }

  override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
    val query =
      "update $CATEGORY_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        return@withContext it.prepareStatement(query).execute()
      }
    }
  }

  override suspend fun getProductsByCategoryName(
    merchantId: Long?,
    lang: Language,
    text: String?
  ): ArrayList<ProductDto> {
    val name: String =
      when (lang) {
        Language.UZ -> "name_uz"
        Language.RU -> "name_ru"
        else -> "name_eng"
      }

    val query =
      """
                select * from $PRODUCT_TABLE_NAME p inner join category c on c.id = p.category_id 
                where p.merchant_id = $merchantId and p.deleted = false and c.deleted = false and c.$name = ? order by  p.created
            """
        .trimIndent()
    val list = ArrayList<ProductDto>()
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              setString(1, text)
              this.closeOnCompletion()
            }
            .executeQuery()
        while (rs.next()) {
          list.add(
            ProductDto(
              id = rs.getLong("id"),
              name =
                TextModel(
                  uz = rs.getString("name_uz"),
                  ru = rs.getString("name_ru"),
                  eng = rs.getString("name_eng")
                ),
              merchantId = rs.getLong("merchant_id"),
            )
          )
        }
      }
    }
    return list
  }
}
