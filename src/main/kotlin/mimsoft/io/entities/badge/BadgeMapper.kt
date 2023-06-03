package mimsoft.io.entities.badge

object BadgeMapper {

    fun toDto(badgeTable: BadgeTable?): BadgeDto? {
        return if (badgeTable==null) null
        else BadgeDto(
            id = badgeTable.id,
            menuId = badgeTable.menuId,
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
            menuId = badgeDto.menuId,
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