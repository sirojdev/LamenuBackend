package mimsoft.io.features.category.repository

import io.ktor.server.testing.*
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.telegram_bot.Language
import mimsoft.io.utils.TextModel

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CategoryRepositoryImplTest {

    private val categoryRepositoryImpl = CategoryRepositoryImpl

    @Test
    fun getAllByClient() = testApplication {
        val merchantId: Long = 1
        val response = categoryRepositoryImpl.getAllByClient(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getCategoryForClientById() = testApplication {
        val merchantId: Long = 6
        val id: Long = 14
        val response = categoryRepositoryImpl.getCategoryForClientById(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 6
        val response = categoryRepositoryImpl.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 6
        val id: Long = 14
        val response = categoryRepositoryImpl.get(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val categoryDto = CategoryDto(
            id = null,
            name = textModel,
            image = "images/2023-07-02-06-15-19-760.jpg",
            merchantId = 1,
            groupId = 4,
            priority = 5,
            products = null
        )
        val response = categoryRepositoryImpl.add(categoryDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val categoryDto = CategoryDto(
            id = null,
            name = textModel,
            image = "images/2023-07-02-06-15-19-760.jpg",
            merchantId = 1,
            groupId = 4,
            priority = 5,
            products = null
        )
    }

    @Test
    fun delete() = testApplication {
        val merchantId: Long = 6
        val id: Long = 14
        val response = categoryRepositoryImpl.delete(id, merchantId)
        assertTrue(response)
    }

    @Test
    fun getCategoryByName() = testApplication {
        val merchantId: Long = 6
        val language = Language.UZ
        val text = "Fastfud"
        val response = categoryRepositoryImpl.getCategoryByName(merchantId, language, text)
        assertNotNull(response)
    }
}