package mimsoft.io.features.category_group

import mimsoft.io.utils.TextModel

object CategoryGroupMapper {
    fun toDto(table: CategoryGroupTable): CategoryGroupDto {
        return CategoryGroupDto(
            id = table.id,
            merchantId = table.merchantId,
            title = TextModel(
                uz = table.titleUz,
                ru = table.titleRu,
                eng = table.titleEng
            ),
            bgColor = table.bgColor,
            textColor = table.textColor
        )
    }

    fun toTable(dto: CategoryGroupDto): CategoryGroupTable {
        return CategoryGroupTable(
            id = dto.id,
            merchantId = dto.merchantId,
            titleUz = dto.title?.uz,
            titleRu = dto.title?.ru,
            titleEng = dto.title?.eng,
            bgColor = dto.bgColor,
            textColor = dto.textColor,
        )
    }
}