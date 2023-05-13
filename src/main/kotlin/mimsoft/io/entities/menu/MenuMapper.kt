package mimsoft.io.entities.menu

import mimsoft.io.utils.TextModel

object MenuMapper {
    fun toMenuTable(menuDto: MenuDto?): MenuTable? {
        return if (menuDto == null) null else MenuTable(
            id = menuDto.id,
            nameUz = menuDto.name?.uz,
            nameRu = menuDto.name?.ru,
            nameEng = menuDto.name?.eng
        )
    }

    fun toMenuDto(menuTable: MenuTable?): MenuDto? {
        return if (menuTable == null) null
        else MenuDto(
            id = menuTable.id,
            name = TextModel(
                uz = menuTable.nameUz,
                ru = menuTable.nameRu,
                eng = menuTable.nameEng
            ),
        )
    }
}