package mimsoft.io.features.story

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.story_info.StoryInfoService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel

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
        val query =
            "select * from $STORY_TABLE_NAME where merchant_id = $merchantId and not deleted order by priority, created"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<StoryDto>()
                while (rs.next()) {
                    val storyInfo = StoryInfoService.getAllByStoryId(storyId = rs.getLong("id"))
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
                        priority = rs.getInt("priority"),
                        stories = storyInfo
                    )
                    list.add(story)
                }
                return@withContext list
            }
        }
    }

    /* suspend fun getAllWithOption(merchantId: Long?): List<StoryDto?> {
        val query = """
            select s.id         s_id,
                s.merchant_id   s_merchant_id,
                   s.priority   s_priority,
                   s.name_uz    s_name_uz,
                   s.name_ru    s_name_ru,
                   s.name_eng   s_name_eng,
                   s.image_uz   s_iuz,
                   s.image_ru   s_iru,
                   s.image_eng  s_ieng,
                   si.id        si_id,
                   si.priority  si_priority,
                   si.image_uz  si_iuz,
                   si.image_ru  si_iru,
                   si.image_eng si_ieng,
                   button_text,
                   button_bg_color,
                   button_text_color
            from story s
                     left join story_info si on si.story_id = s.id
            where s.merchant_id = $merchantId 
              and s.deleted = false
            order by s.priority, s.created
            
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<StoryDto>()
                var newStoryId: Long? = null
                var oldStoryId: Long? = null
                while (rs.next()) {
                    newStoryId = rs.getLong("s_id")

                    val story = StoryDto(
                        id = rs.getLong("s_id"),
                        merchantId = rs.getLong("s_merchant_id"),
                        name = TextModel(
                            uz = rs.getString("s_name_uz"),
                            ru = rs.getString("s_name_ru"),
                            eng = rs.getString("s_name_eng")
                        ),
                        image = TextModel(
                            uz = rs.getString("s_iuz"),
                            ru = rs.getString("s_iru"),
                            eng = rs.getString("s_ieng")
                        ),
                        priority = rs.getInt("s_priority")
                    )

                    val storyIn = StoryInfoDto(

                    )

                    if ()
                        story.stories

                    list.add(story)
                }
                return@withContext list
            }
        }
    }*/

    suspend fun delete(merchantId: Long?, id: Long?): Boolean {
        var rs = 0
        val query = """
            update story
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun updatePriority(priorityNumber: Long?, id: Long?, merchantId: Long?): Boolean {
        var rs = 0
        val query =
            "update $STORY_TABLE_NAME set priority = $priorityNumber where id = $id and merchant_id = $merchantId and not deleted"
        withContext(DBManager.databaseDispatcher) {
            rs = repository.connection().use {
                it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}