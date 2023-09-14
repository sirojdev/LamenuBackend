package mimsoft.io.features.option.repository

import io.ktor.server.testing.*
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.option.OptionTable
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OptionRepositoryImplTest {


    val optionalRepositoryImpl = OptionRepositoryImpl

    @Test
    fun getSubOptions() = testApplication {
        val id: Long = 1
        val response = optionalRepositoryImpl.getSubOptions(id)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = optionalRepositoryImpl.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun getOptionsByProductId() = testApplication {
        val merchantId: Long = 1
        val productId: Long = 23
        val response = optionalRepositoryImpl.getOptionsByProductId(merchantId, productId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 12
        val merchantId: Long = 1
        val response = optionalRepositoryImpl.get(id, merchantId)
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
            merchantId = 1,
            jowiId = "",
            parentId = 12,
            productId = 32,
            name = textModel,
            image = "Image",
            price = 23423

        )
        val response = optionalRepositoryImpl.update(optionDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 1
        val merchantId: Long = 1
        val response = optionalRepositoryImpl.delete(id, merchantId)
        assertTrue(response)
    }
}