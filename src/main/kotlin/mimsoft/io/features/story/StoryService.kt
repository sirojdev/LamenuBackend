package mimsoft.io.features.story

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.ADDRESS_TABLE_NAME
import mimsoft.io.features.address.repository.AddressRepositoryImpl
import mimsoft.io.features.book.repository.BookResponseDto
import mimsoft.io.features.book.repository.BookServiceImpl
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

object StoryService {
    private val repository: BaseRepository = DBManager
    private val mapper = StoryMapper
    suspend fun add(storyDto: StoryDto): Long? {
        return repository.postData(
            dataClass = StoryTable::class,
            dataObject = mapper.toTable(storyDto),
            tableName = STORY_TABLE_NAME
        )
    }

    suspend fun update(storyDto: StoryDto): Boolean =
        repository.updateData(
            dataClass = StoryTable::class,
            dataObject = mapper.toTable(storyDto),
            tableName = STORY_TABLE_NAME
        )

    suspend fun getById(merchantId: Long?, id: Long?): StoryDto? {
        val data = repository.getPageData(
            dataClass = StoryTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = STORY_TABLE_NAME
        )?.data?.firstOrNull()
        if (data == null) {
            return null
        }
        return mapper.toDto(data)
    }

    suspend fun getAll(merchantId: Long?): List<StoryDto?> {
        val gson = Gson()
        val query = "select * from $STORY_TABLE_NAME where merchant_id = $merchantId and not deleted order by priority, created"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<StoryDto>()
                while (rs.next()) {
                    val prod = rs.getString("products")
                    val typeToken = object : TypeToken<List<ProductDto>>() {}.type
                    val prodList = gson.fromJson<List<ProductDto>>(prod, typeToken)
                    val story = StoryDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng")
                        ),
                        image = TextModel(
                            uz = rs.getString("image_uz"),
                            ru = rs.getString("image_ru"),
                            eng = rs.getString("image_eng")
                        ),
                        products = prodList,
                        priority = rs.getInt("priority")
                    )
                    list.add(story)
                }
                return@withContext list
            }
        }
    }

    suspend fun delete(merchantId: Long?, id: Long?): Boolean {
        val query = "update $STORY_TABLE_NAME " +
                "set deleted = true where " +
                "id = $id and " +
                "merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    suspend fun updatePriority(priorityNumber: Long?, id: Long?, merchantId: Long?): Boolean {
        val query =
            "update $STORY_TABLE_NAME set priority = $priorityNumber where id = $id and merchant_id = $merchantId and not deleted"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).executeUpdate()
            }
        }
        return true
    }


}