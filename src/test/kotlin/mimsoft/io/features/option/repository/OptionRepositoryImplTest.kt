package mimsoft.io.features.option.repository

import io.ktor.server.testing.*
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.option.OptionTable
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OptionRepositoryImplTest {


    private val optionalRepositoryImpl = OptionRepositoryImpl

    @Test
    fun getSubOptions() = testApplication {
        val id: Long = 17
        val response = optionalRepositoryImpl.getSubOptions(id)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = optionalRepositoryImpl.getAll(merchantId, 31)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun getOptionsByProductId() = testApplication {
        val merchantId: Long = 1
        val productId: Long = 20
        val response = optionalRepositoryImpl.getOptionsByProductId(merchantId, productId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 17
        val merchantId: Long = 1
        val response = optionalRepositoryImpl.get(id, merchantId)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val optionTable = OptionTable(
            merchantId = 2,
            parentId = 7,
            nameUz = "Uz",
            nameRu = "Ru",
            nameEng = "Eng",
            image = "Image path",
            price = 23433,
            productId = 34
        )
        val response = optionalRepositoryImpl.add(optionTable)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "Uz",
            ru = "Ru",
            eng = "Eng",
        )
        val optionDto = OptionDto(
            id = 3999,
            merchantId = 2,
            jowiId = "",
            parentId = 12,
            productId = 32,
            name = textModel,
            image = "Image",
            price = 23423
        )
        val response = optionalRepositoryImpl.update(optionDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 39
        val merchantId: Long = 2
        val response = optionalRepositoryImpl.delete(id, merchantId, 31)
        if (response)
            assertTrue(response)
    }
}