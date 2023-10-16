package mimsoft.io.features.label.repository

import io.ktor.server.testing.*
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelTable
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LabelRepositoryImplTest {

    private val labelRepositoryImpl = LabelRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = labelRepositoryImpl.getAll(merchantId, 31)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 1422
        val merchantId: Long = 6
        val response = labelRepositoryImpl.get(id, merchantId, 31)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val labelTable = LabelTable(
            merchantId = 1,
            nameUz = "StringUz",
            nameRu = "StringRu",
            nameEng = "StringEng",
            textColor = "#ffffff",
            bgColor = "#00000",
            icon = "images/2023-06-08-09-55-59-045.jpg"
        )
        val response = labelRepositoryImpl.add(labelTable)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "Uz",
            ru = "Ru",
            eng = "Eng"
        )
        val labelDto = LabelDto(
            id = 1128,
            merchantId = 1,
            name = textModel,
            textColor = "#ffffff",
            bgColor = "#000000",
            icon = "images/2023-06-08-09-55-59-045.jpg"
        )
        val response = labelRepositoryImpl.update(labelDto)
        if (response)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 14
        val merchantId: Long = 6
        val response = labelRepositoryImpl.delete(id, merchantId, 31)
        if (response)
            assertTrue(response)
    }
}