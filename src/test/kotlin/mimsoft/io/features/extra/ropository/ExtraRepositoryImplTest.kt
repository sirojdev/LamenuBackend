package mimsoft.io.features.extra.ropository

import io.ktor.server.testing.*
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull

class ExtraRepositoryImplTest {

    val extraRepositoryImpl = ExtraRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = extraRepositoryImpl.getAll(merchantId)
        assertNotNull(response.firstOrNull())
    }

    @Test
    fun get() = testApplication {
        val merchantId: Long = 1
        val id: Long = 1
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
    fun add() {
    }

    @Test
    fun getExtrasByProductId() {
    }

    @Test
    fun delete() {
    }
}