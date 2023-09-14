package mimsoft.io.features.news.repository

import io.ktor.server.testing.*
import mimsoft.io.features.news.NewsDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class NewsRepositoryImplTest {

    val newsRepositoryImpl = NewsRepositoryImpl

    @Test
    fun add() = testApplication {
        val textModel = TextModel(
            uz = "Uz",
            ru = "Ru",
            eng = "Eng",
        )
        val newsDto = NewsDto(
            merchantId = 1,
            title = textModel
        )
        newsRepositoryImpl.add(newsDto)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "Uz",
            ru = "Ru",
            eng = "Eng",
        )
        val newsDto = NewsDto(
            id = 17,
            merchantId = 1,
            title = textModel
        )
        val response = newsRepositoryImpl.update(newsDto)
        assertTrue(response)
    }

    @Test
    fun getById() = testApplication {
        val id: Long = 17
        val merchantId: Long = 1
        val response = NewsRepositoryImpl.getById(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val limit = 1
        val offset = 1
        val response = newsRepositoryImpl.getAll(merchantId, limit, offset)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 1
        val merchantId: Long = 1
        val response = newsRepositoryImpl.delete(id, merchantId)
        assertTrue(response)
    }
}