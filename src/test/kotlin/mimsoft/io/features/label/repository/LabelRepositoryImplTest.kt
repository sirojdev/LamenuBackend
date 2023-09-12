package mimsoft.io.features.label.repository

import io.ktor.server.testing.*
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelTable
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LabelRepositoryImplTest {

    val labelRepositoryImpl = LabelRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = labelRepositoryImpl.getAll(merchantId)
        assertNotNull(merchantId)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 12
        val merchantId: Long = 1
        val response = labelRepositoryImpl.get(id, merchantId)
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
            merchantId = 1,
            name = textModel,
            textColor = "#ffffff",
            bgColor = "#000000",
            icon = "images/2023-06-08-09-55-59-045.jpg"
        )
        val response = labelRepositoryImpl.update(labelDto)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 1
        val merchantId: Long = 1
        val response = labelRepositoryImpl.delete(id, merchantId)
        assertTrue(response)
    }
}