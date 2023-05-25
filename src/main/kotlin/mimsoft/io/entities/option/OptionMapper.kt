package mimsoft.io.entities.option

import mimsoft.io.utils.TextModel

object OptionMapper {
    fun toOptionTable(optionDto: OptionDto?): OptionTable? {
        return if (optionDto == null) null else OptionTable(
            id = optionDto.id,
            nameUz = optionDto.name?.uz,
            nameRu = optionDto.name?.ru,
            nameEn = optionDto.name?.en,
            descriptionUz = optionDto.description?.uz,
            descriptionRu = optionDto.description?.ru,
            descriptionEn = optionDto.description?.en,
            image = optionDto.image,
            price = optionDto.price
        )
    }

    fun toOptionDto(optionTable: OptionTable?): OptionDto? {
        return if (optionTable == null) null
        else OptionDto(
            id = optionTable.id,
            name = TextModel(
                uz = optionTable.nameUz,
                ru = optionTable.nameRu,
                en = optionTable.nameEn
            ),
            description = TextModel(
                uz = optionTable.descriptionUz,
                ru = optionTable.descriptionRu,
                en = optionTable.descriptionEn
            ),
            image = optionTable.image,
            price = optionTable.price
        )
    }
}