package mimsoft.io.entities.category

import mimsoft.io.utils.TextModel

object CategoryMapper {
    fun toCategoryTable(categoryDto: CategoryDto?): CategoryTable? {
        return if (categoryDto==null) null
        else CategoryTable(
            id = categoryDto.id,
            nameUz = categoryDto.name?.uz,
            nameRu = categoryDto.name?.ru,
            nameEn = categoryDto.name?.eng,
            image = categoryDto.image
        )
    }

    fun toCategoryDto(categoryTable: CategoryTable?): CategoryDto? {
        return if (categoryTable==null) null
        else CategoryDto(
            id = categoryTable.id,
            name = TextModel(
                uz = categoryTable.nameUz,
                ru = categoryTable.nameRu,
                eng = categoryTable.nameEn
            ),
            image = categoryTable.image
        )
    }
}
