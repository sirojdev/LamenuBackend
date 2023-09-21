package mimsoft.io.features.extra.ropository

import io.ktor.server.testing.*
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.extra.ExtraTable
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull

class ExtraRepositoryImplTest {

    private val extraRepositoryImpl = ExtraRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = extraRepositoryImpl.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 26
        val response = extraRepositoryImpl.get(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "StringUz",
            ru = "StringRu",
            eng = "StringEng"
        )
        val extraDto = ExtraDto(
            id = 4,
            image = null,
            price = null,
            merchantId = null,
            name = null,
            productId = 63
        )
        extraRepositoryImpl.update(extraDto)
    }

    @Test
    fun add() = testApplication {
        val extraTable = ExtraTable(
            image = "images/2023-08-08-14-23-51-465.png",
            nameUz = "Shakar",
            nameEng = "Сахар",
            nameRu = "Sugar",
            price = 1224,
            merchantId = 1,
            productId = 108
        )
        val response = extraRepositoryImpl.add(extraTable)
        assertNotNull(response)
    }

    @Test
    fun getExtrasByProductId() = testApplication {
        val merchantId: Long = 1
        val productId: Long = 1
        val response = extraRepositoryImpl.getExtrasByProductId(merchantId, productId)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 12
        val merchantId: Long = 1
        val response = extraRepositoryImpl.delete(id, merchantId)
        assertNotNull(response)
    }
}