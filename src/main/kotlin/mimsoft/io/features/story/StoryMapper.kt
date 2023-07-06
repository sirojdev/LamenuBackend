package mimsoft.io.features.story

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel

object StoryMapper {
    fun toDto(storyTable: StoryTable): StoryDto {
        val gson = Gson()
        val prod = storyTable.products
        val type = object : TypeToken<List<ProductDto>>() {}.type
        val productList = gson.fromJson<List<ProductDto>>(prod, type)
        return StoryDto(
            id = storyTable.id,
            merchantId = storyTable.merchantId,
            name = TextModel(
                uz = storyTable.nameUz,
                ru = storyTable.nameRu,
                eng = storyTable.nameEng
            ),
            image = TextModel(
                uz = storyTable.imageUz,
                ru = storyTable.imageRu,
                eng = storyTable.imageEng
            ),
            products = productList,
            priority = storyTable.priority
        )
    }

    fun toTable(storyDto: StoryDto): StoryTable {
        return StoryTable(
            id = storyDto.id,
            merchantId = storyDto.merchantId,
            nameUz = storyDto.name?.uz,
            nameRu = storyDto.name?.ru,
            nameEng = storyDto.name?.eng,
            imageUz = storyDto.image?.uz,
            imageRu = storyDto.image?.ru,
            imageEng = storyDto.image?.eng,
            priority = storyDto.priority,
            products = Gson().toJson(storyDto.products)
        )
    }
}
