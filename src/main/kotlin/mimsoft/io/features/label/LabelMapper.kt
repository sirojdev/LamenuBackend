package mimsoft.io.features.label

import mimsoft.io.utils.TextModel

object LabelMapper {
    fun toLabelTable(labelDto: LabelDto?): LabelTable? {
        return if (labelDto == null) null else LabelTable(
            id = labelDto.id,
            merchantId = labelDto.merchantId,
            nameUz = labelDto.name?.uz,
            nameRu = labelDto.name?.ru,
            nameEn = labelDto.name?.en,
            textColor = labelDto.textColor,
            bgColor = labelDto.bgColor,
            icon = labelDto.icon
        )
    }

    fun toLabelDto(labelTable: LabelTable?): LabelDto? {
        return if (labelTable == null) null
        else LabelDto(
            id = labelTable.id,
            name = TextModel(
                uz = labelTable.nameUz,
                ru = labelTable.nameRu,
                en = labelTable.nameEn
            ),
            merchantId = labelTable.merchantId,
            textColor = labelTable.textColor,
            bgColor = labelTable.bgColor,
            icon = labelTable.icon
        )
    }
}