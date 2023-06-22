package mimsoft.io.features.cashback

import mimsoft.io.utils.TextModel

object CashbackMapper {
    fun toDto(table: CashbackTable): CashbackDto {
        return CashbackDto(
            merchantId = table.merchantId,
            id = table.id,
            name = TextModel(
                uz = table.nameUz,
                ru = table.nameRu,
                eng = table.nameEng,
            ),
            maxCost = table.maxCost,
            minCost = table.minCost
        )
    }

    fun toTable(dto: CashbackDto): CashbackTable {
        return CashbackTable(
            id = dto.id,
            merchantId = dto.merchantId,
            maxCost = dto.maxCost,
            minCost = dto.minCost,
            nameUz = dto.name?.uz,
            nameRu = dto.name?.ru,
            nameEng = dto.name?.eng
        )
    }
}