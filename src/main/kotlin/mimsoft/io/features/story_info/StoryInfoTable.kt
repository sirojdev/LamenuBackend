package mimsoft.io.features.story_info

import java.sql.Timestamp

const val STORY_INFO_TABLE_NAME = "story_info"
data class StoryInfoTable(
    val id: Long? = null,
    val priority: Int? = null,
    val storyId: Long? = null,
    val merchantId: Long? = null,
    val products: String? = null,
    val imageUz: String? = null,
    val imageRu: String? = null,
    val imageEng: String? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val buttonTextUz: String? = null,
    val buttonTextRu: String? = null,
    val buttonTextEng: String? = null,
    val buttonBgColor: String? = null,
    val buttonTextColor: String? = null
)
