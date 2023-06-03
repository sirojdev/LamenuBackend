package mimsoft.io.features.category

import mimsoft.io.utils.TextModel

object CategoryMapper {
    fun toCategoryTable(categoryDto: CategoryDto?): CategoryTable? {
        return if (categoryDto == null) null
        else CategoryTable(
            id = categoryDto.id,
            nameUz = categoryDto.name?.uz,
            nameRu = categoryDto.name?.ru,
            nameEng = categoryDto.name?.eng,
            image = categoryDto.image,
            merchantId = categoryDto.merchantId,
            bgColor = categoryDto.bgColor,
            textColor = categoryDto.textColor
        )
    }

    fun toCategoryDto(categoryTable: CategoryTable?): CategoryDto? {
        return if (categoryTable == null) null
        else CategoryDto(
            id = categoryTable.id,
            name = TextModel(
                uz = categoryTable.nameUz,
                ru = categoryTable.nameRu,
                eng = categoryTable.nameEng
            ),
            image = categoryTable.image ,
            merchantId = categoryTable.merchantId,
            bgColor = categoryTable.bgColor,
            textColor = categoryTable.textColor
        )
    }
}
