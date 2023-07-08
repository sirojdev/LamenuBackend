package mimsoft.io.features.story_info

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.TextModel

object StoryInfoMapper {
    fun toDto(table: StoryInfoTable): StoryInfoDto {
        val gson = Gson()
        val product = table.products
        val type = object : TypeToken<List<ProductDto>>(){}.type
        val list = gson.fromJson<List<ProductDto>>(product, type)
        return StoryInfoDto(
            id = table.id,
            merchantId = table.merchantId,
            image = TextModel(
                uz = table.imageUz,
                ru = table.imageRu,
                eng = table.imageEng
            ),
            priority = table.priority,
            products = list,
            buttonBgColor = table.buttonBgColor,
            buttonTextColor = table.buttonTextColor,
            buttonText = TextModel(
                uz = table.buttonTextUz,
                ru = table.buttonTextRu,
                eng = table.buttonTextEng
            ),
            storyId = table.storyId
        )
    }

    fun toTable(storyDto: StoryInfoDto): StoryInfoTable {
        return StoryInfoTable(
            id = storyDto.id,
            merchantId = storyDto.merchantId,
            imageUz = storyDto.image?.uz,
            imageRu = storyDto.image?.ru,
            imageEng = storyDto.image?.eng,
            priority = storyDto.priority,
            storyId = storyDto.storyId,
            products = Gson().toJson(storyDto.products),
            buttonTextUz = storyDto.buttonText?.uz,
            buttonTextRu = storyDto.buttonText?.ru,
            buttonTextEng = storyDto.buttonText?.eng,
            buttonBgColor = storyDto.buttonBgColor,
            buttonTextColor = storyDto.buttonTextColor
        )
    }
}
