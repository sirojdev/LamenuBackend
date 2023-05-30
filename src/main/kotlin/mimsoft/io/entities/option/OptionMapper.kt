package mimsoft.io.entities.option

import mimsoft.io.utils.TextModel

object OptionMapper {
    fun toOptionTable(optionDto: OptionDto?): OptionTable? {
        return if (optionDto == null) null else OptionTable(
            id = optionDto.id,
            nameUz = optionDto.name?.uz,
            nameRu = optionDto.name?.ru,
            nameEng = optionDto.name?.eng,
            descriptionUz = optionDto.description?.uz,
            descriptionRu = optionDto.description?.ru,
            descriptionEng = optionDto.description?.eng,
            image = optionDto.image,
            price = optionDto.price,
        )
    }

    fun toOptionDto(optionTable: OptionTable?): OptionDto? {
        return if (optionTable == null) null
        else OptionDto(
            id = optionTable.id,
            name = TextModel(
                uz = optionTable.nameUz,
                ru = optionTable.nameRu,
                eng = optionTable.nameEng
            ),
            description = TextModel(
                uz = optionTable.descriptionUz,
                ru = optionTable.descriptionRu,
                eng = optionTable.descriptionEng
            ),
            image = optionTable.image,
            price = optionTable.price
        )
    }
}