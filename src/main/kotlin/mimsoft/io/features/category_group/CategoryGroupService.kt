package mimsoft.io.features.category_group

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.repository.BookResponseDto
import mimsoft.io.features.book.repository.BookServiceImpl
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.table.TableDto
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
        val query = "select * from $CATEGORY_GROUP_TABLE where merchant_id = $merchantId and id = $id and deleted = false"
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
                            merchantId = rs.getLong("merchant_id")
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
       (SELECT json_agg(json_build_object(
               'id', c.id,
               'bgColor', c.bg_color,
               'nameUz', c.name_uz,
               'nameRu', c.name_ru,
               'nameEng', c.name_eng,
               'image', c.image,
               'textColor', c.text_color
           ))
        FROM category c
        WHERE c.group_id = cg.id
          AND c.merchant_id = 1) AS categories
FROM category_group cg
WHERE cg.merchant_id = 1
  and cg.deleted = false
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val gson = Gson()
                val data = arrayListOf<CategoryGroupClientDto>()
                while (rs.next()) {
                    val categories = rs.getString("categories")
                    val typeToken = object : TypeToken<List<CategoryDto>>(){}.type
                    val list = gson.fromJson<List<CategoryDto>>(categories, typeToken)
                    val book = CategoryGroupClientDto(
                        id = rs.getLong("id"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        ),
                        categories = list,
                        bgColor = rs.getString("bg_color"),
                    )
                    data.add(book)
                }
                return@withContext data
            }
        }

    }
}