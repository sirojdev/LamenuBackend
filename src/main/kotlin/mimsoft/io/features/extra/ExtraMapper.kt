package mimsoft.io.features.extra

import mimsoft.io.utils.TextModel

object ExtraMapper {
    fun toExtraTable(extraDto: ExtraDto?): ExtraTable? {
        return if (extraDto==null) null
        else ExtraTable(
            id = extraDto.id,
            nameUz = extraDto.name?.uz,
            nameRu = extraDto.name?.ru,
            nameEng = extraDto.name?.eng,
            price = extraDto.price,
            descriptionUz = extraDto.description?.uz,
            descriptionRu = extraDto.description?.ru,
            descriptionEng = extraDto.description?.eng
        )
    }

    fun toExtraDto(extraTable: ExtraTable?): ExtraDto? {
        return if (extraTable==null) null
        else ExtraDto(
            id = extraTable.id,
            name = TextModel(
                uz = extraTable.nameUz,
                ru = extraTable.nameRu,
                eng = extraTable.nameEng
            ),
            price = extraTable.price,
            description = TextModel(
                uz = extraTable.descriptionUz,
                ru = extraTable.descriptionRu,
                eng = extraTable.descriptionEng
            )
        )
    }
}
