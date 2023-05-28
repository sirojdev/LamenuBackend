package mimsoft.io.entities.label

import mimsoft.io.utils.TextModel

object LabelMapper {
    fun toLabelTable(labelDto: LabelDto?): LabelTable? {
        return if (labelDto == null) null else LabelTable(
            id = labelDto.id,
            menuId = labelDto.menuId,
            nameUz = labelDto.name?.uz,
            nameRu = labelDto.name?.ru,
            nameEn = labelDto.name?.eng,
            textColor = labelDto.textColor,
            bgColor = labelDto.bgColor,
            icon = labelDto.icon
        )
    }

    fun toLabelDto(labelTable: LabelTable?): LabelDto? {
        return if (labelTable == null) null
        else LabelDto(
            id = labelTable.id,
            menuId = labelTable.menuId,
            name = TextModel(
                uz = labelTable.nameUz,
                ru = labelTable.nameRu,
                eng = labelTable.nameEn
            ),
            textColor = labelTable.textColor,
            bgColor = labelTable.bgColor,
            icon = labelTable.icon
        )
    }
}