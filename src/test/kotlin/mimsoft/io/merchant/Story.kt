package mimsoft.io.merchant

import mimsoft.io.features.story.StoryDto
import mimsoft.io.features.story.StoryService
import mimsoft.io.utils.TextModel
import org.junit.Test

class Story {
  @Test
  suspend fun addNew() {
    val story =
      StoryDto(
        name = TextModel(uz = "UzName", ru = "RuName", eng = "EngName"),
        image = TextModel(uz = "UzImage", ru = "RuImage", eng = "EngImage"),
        priority = 2
      )

    StoryService.add(story)
  }
}
