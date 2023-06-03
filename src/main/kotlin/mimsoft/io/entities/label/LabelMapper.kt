package mimsoft.io.entities.label

import mimsoft.io.utils.TextModel

object LabelMapper {
    fun toLabelTable(labelDto: LabelDto?): LabelTable? {
        return if (labelDto == null) null else LabelTable(
            id = labelDto.id,
            nameUz = labelDto.name?.uz,
            nameRu = labelDto.name?.ru,
            nameEng = labelDto.name?.eng,
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
                eng = labelTable.nameEng
            ),
            textColor = labelTable.textColor,
            bgColor = labelTable.bgColor,
            icon = labelTable.icon
        )
    }
}