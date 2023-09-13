package mimsoft.io.features.news.repository

import io.ktor.server.testing.*
import mimsoft.io.features.news.NewsDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test


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
    fun update() {
    }

    @Test
    fun getById() {
    }

    @Test
    fun getAll() {
    }

    @Test
    fun delete() {
    }
}