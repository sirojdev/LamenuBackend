package mimsoft.io.features.badge

object BadgeMapper {

    fun toDto(badgeTable: BadgeTable?): BadgeDto? {
        return if (badgeTable==null) null
        else BadgeDto(
            id = badgeTable.id,
            name = badgeTable.name,
            textColor = badgeTable.textColor,
            bgColor = badgeTable.bgColor,
            icon = badgeTable.icon,
        )
    }

    fun toTable(badgeDto: BadgeDto?): BadgeTable? {
        return if (badgeDto==null) null
            else BadgeTable(
            id = badgeDto.id,
            name = badgeDto.name,
            textColor = badgeDto.textColor,
            bgColor = badgeDto.bgColor,
            icon = badgeDto.icon,
        )
    }

    fun toDtoList(badgeTableList: List<BadgeTable>): List<BadgeDto?> {
        return badgeTableList.map { toDto(it) }
    }

    fun toTableList(badgeDtoList: List<BadgeDto>): List<BadgeTable?> {
        return badgeDtoList.map { toTable(it) }
    }
}