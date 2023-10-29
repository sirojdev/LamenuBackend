package mimsoft.io.features.story_info

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object StoryInfoService {
  private val repository: BaseRepository = DBManager
  private val mapper = StoryInfoMapper

  suspend fun add(storyDto: StoryInfoDto): Long? {
    return repository.postData(
      dataClass = StoryInfoTable::class,
      dataObject = mapper.toTable(storyDto),
      tableName = STORY_INFO_TABLE_NAME
    )
  }

  suspend fun update(storyDto: StoryInfoDto): Boolean =
    repository.updateData(
      dataClass = StoryInfoTable::class,
      dataObject = mapper.toTable(storyDto),
      tableName = STORY_INFO_TABLE_NAME
    )

  suspend fun getById(merchantId: Long?, id: Long?): StoryInfoDto? {
    val data =
      repository
        .getPageData(
          dataClass = StoryInfoTable::class,
          where = mapOf("merchant_id" to merchantId as Any, "id" to id as Any),
          tableName = STORY_INFO_TABLE_NAME
        )
        ?.data
        ?.firstOrNull()
    if (data == null) {
      return null
    }
    return mapper.toDto(data)
  }

  suspend fun getAll(merchantId: Long?): List<StoryInfoDto> {
    val query =
      "select * from $STORY_INFO_TABLE_NAME where merchant_id = $merchantId and not deleted order by priority, created"
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<StoryInfoDto>()
        while (rs.next()) {
          val story =
            StoryInfoTable(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              imageUz = rs.getString("image_uz"),
              imageRu = rs.getString("image_ru"),
              imageEng = rs.getString("image_eng"),
              priority = rs.getInt("priority"),
              storyId = rs.getLong("story_id"),
              products = rs.getString("products"),
              buttonTextColor = rs.getString("button_text_color"),
              buttonTextUz = rs.getString("button_text_uz"),
              buttonTextRu = rs.getString("button_text_ru"),
              buttonTextEng = rs.getString("button_text_eng"),
              buttonBgColor = rs.getString("button_bg_color")
            )
          list.add(mapper.toDto(story))
        }
        return@withContext list
      }
    }
  }

  suspend fun getAllByStoryId(storyId: Long?): List<StoryInfoDto> {
    val query = "select * from $STORY_INFO_TABLE_NAME where story_id = $storyId and not deleted"
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<StoryInfoDto>()
        while (rs.next()) {
          val story =
            StoryInfoTable(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              imageUz = rs.getString("image_uz"),
              imageRu = rs.getString("image_ru"),
              imageEng = rs.getString("image_eng"),
              priority = rs.getInt("priority"),
              storyId = rs.getLong("story_id"),
              products = rs.getString("products"),
              buttonTextColor = rs.getString("button_text_color"),
              buttonTextUz = rs.getString("button_text_uz"),
              buttonTextRu = rs.getString("button_text_ru"),
              buttonTextEng = rs.getString("button_text_eng"),
              buttonBgColor = rs.getString("button_bg_color")
            )
          list.add(mapper.toDto(story))
        }
        return@withContext list
      }
    }
  }

  suspend fun delete(merchantId: Long?, id: Long?): Boolean {
    val query =
      "update $STORY_INFO_TABLE_NAME " +
        "set deleted = true where " +
        "id = $id and " +
        "merchant_id = $merchantId"
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).execute()
      }
    }
    return true
  }

  suspend fun updatePriority(priorityNumber: Long?, id: Long?, merchantId: Long?): Boolean {
    val query =
      "update $STORY_INFO_TABLE_NAME set priority = $priorityNumber where id = $id and merchant_id = $merchantId and not deleted"
    withContext(Dispatchers.IO) {
      repository.connection().use { it.prepareStatement(query).executeUpdate() }
    }
    return true
  }
}
