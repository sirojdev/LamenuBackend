package mimsoft.io.features.story

import mimsoft.io.features.story_info.StoryInfoDto
import mimsoft.io.utils.TextModel

data class StoryDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val image: TextModel? = null,
    val priority: Int? = null,
    val stories: List<StoryInfoDto>? = null
)
