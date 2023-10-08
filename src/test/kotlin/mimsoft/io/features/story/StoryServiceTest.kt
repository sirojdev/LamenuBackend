package mimsoft.io.features.story

import io.ktor.server.testing.*
import mimsoft.io.features.story_info.StoryInfoDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StoryServiceTest {

    private val storyService = StoryService

    @Test
    fun add() = testApplication {
        val textModelName = TextModel(
            uz = "Chegirmalr7",
            ru = "Скидки7",
            eng = "Sales7",
        )
        val textModelImage = TextModel(
            uz = "images/2023-07-08-11-39-38-59777.jpg",
            ru = "images/2023-07-08-11-39-38-59777.jpg",
            eng = "images/2023-07-08-11-39-38-59777.jpg",
        )
        val storyDto = StoryDto(
            merchantId = 1,
            name = textModelName,
            image = textModelImage,
            priority = 7
        )
        val response = storyService.add(storyDto)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModelName = TextModel(
            uz = "Chegirmalr788",
            ru = "Скидки7",
            eng = "Sales7",
        )
        val textModelImage = TextModel(
            uz = "images/2023-07-08-11-39-38-59777.jpg",
            ru = "images/2023-07-08-11-39-38-59777.jpg",
            eng = "images/2023-07-08-11-39-38-59777.jpg",
        )
        val storyInfoDto = listOf(
            StoryInfoDto(
                id = 12
            )
        )
        val storyDto = StoryDto(
            id = 666,
            merchantId = 1,
            name = textModelName,
            image = textModelImage,
            priority = 7,
            stories = storyInfoDto
        )
        val response = storyService.update(storyDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun getById() = testApplication {
        val merchantId: Long = 1
        val id: Long = 666
        val response = storyService.getById(merchantId, id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 111
        val response = storyService.getAll(merchantId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val merchantId: Long = 1
        val id: Long = 6
        val response = storyService.delete(merchantId, id)
        if (response)
            assertTrue(response)
    }

    @Test
    fun updatePriority() = testApplication {
        val priorityNumber: Long = 78
        val id: Long = 66
        val merchantId: Long = 1
        val response = storyService.updatePriority(priorityNumber, id, merchantId)
        if (response)
            assertTrue(response)
    }
}