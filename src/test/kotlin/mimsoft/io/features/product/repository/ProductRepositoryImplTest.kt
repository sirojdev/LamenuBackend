package mimsoft.io.features.product.repository

import io.ktor.server.testing.*
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.utils.TextModel
import kotlin.test.Test
import kotlin.test.assertNotNull

class ProductRepositoryImplTest {

    private val productRepositoryImpl = ProductRepositoryImpl

    @Test
    fun getAllProductInfo() = testApplication {
        val merchantId: Long = 111
        val response = productRepositoryImpl.getAllProductInfo(merchantId)
        assert(response.isEmpty())
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val search = "test"
        val response = productRepositoryImpl.getAll(merchantId, search)
        assert(response.isEmpty())
    }

    @Test
    fun get() = testApplication {
        val id: Long = 95
        val merchantId: Long = 1
        val response = productRepositoryImpl.get(merchantId, id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val productTable = ProductTable(
            merchantId = 1,
            nameUz = "StringUz",
            nameRu = "StringRu",
            nameEng = "StringEng",
            descriptionUz = "desUz",
            descriptionRu = "desRu",
            descriptionEng = "desEng",
            categoryId = 13,
            image = "https://images.theconversation.com/files/368263/original/file-20201109-22-lqiq5c.jpg",
            costPrice = 11,
            idRKeeper = -13,
            idJowi = -1,
            idJoinPoster = 98,
            timeCookingMax = 10,
            timeCookingMin = 5
        )
        val response = productRepositoryImpl.add(productTable)
        println("rs: $response")
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "Uz",
            ru = "Ru",
            eng = "Eng"
        )
        val categoryDto = CategoryDto(
            id = 14
        )
        val productIntegrationDto = ProductIntegrationDto(
            id = 17
        )
        val optionDto = listOf(
            OptionDto(
                id = 17
            )
        )
        val extraDto = listOf(
            ExtraDto(
                id = 11
            )
        )
        val labelDto = listOf(
            LabelDto(
                id = 12
            )
        )
        val productDto = ProductDto(
            id = 12211,
            merchantId = 1,
            name = textModel,
            description = textModel,
            image = "image",
            costPrice = 123,
            category = categoryDto,
            productIntegration = productIntegrationDto, timeCookingMax = 20,
            timeCookingMin = 15,
            count = 21,
            discount = 23,
            options = optionDto,
            extras = extraDto,
            labels = labelDto,
            joinPosterId = 34,
            jowiPosterId = "22"
        )
        val response = productRepositoryImpl.update(productDto)
        if (response)
            assertNotNull(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 122
        val merchantId: Long = 1
        val response = productRepositoryImpl.delete(id, merchantId)
        println("rs: $response")
    }

    @Test
    fun getProductInfo() {
    }

    @Test
    fun getAllByCategories() {
    }

    @Test
    fun getByName() {
    }
}