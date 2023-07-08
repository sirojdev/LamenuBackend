package mimsoft.io.features.story

import java.sql.Timestamp

const val STORY_TABLE_NAME = "story"
data class StoryTable(
    val id: Long? = null,
    val priority: Int? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val imageRu: String? = null,
    val nameEng: String? = null,
    val imageUz: String? = null,
    val deleted: Boolean? = null,
    val imageEng: String? = null,
    val merchantId: Long? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
)
