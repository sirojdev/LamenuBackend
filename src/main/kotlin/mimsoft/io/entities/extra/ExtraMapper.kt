package mimsoft.io.entities.extra

import mimsoft.io.utils.TextModel

object ExtraMapper {
    fun toExtraTable(extraDto: ExtraDto?): ExtraTable? {
        return if (extraDto==null) null
        else ExtraTable(
            id = extraDto.id,
            nameUz = extraDto.name?.uz,
            nameRu = extraDto.name?.ru,
            nameEn = extraDto.name?.en,
            price = extraDto.price,
            descriptionUz = extraDto.description?.uz,
            descriptionRu = extraDto.description?.ru,
            descriptionEn = extraDto.description?.en
        )
    }

    fun toExtraDto(extraTable: ExtraTable?): ExtraDto? {
        return if (extraTable==null) null
        else ExtraDto(
            id = extraTable.id,
            name = TextModel(
                uz = extraTable.nameUz,
                ru = extraTable.nameRu,
                en = extraTable.nameEn
            ),
            price = extraTable.price,
            description = TextModel(
                uz = extraTable.descriptionUz,
                ru = extraTable.descriptionRu,
                en = extraTable.descriptionEn
            )
        )
    }
}
