package mimsoft.io.features.payment_type

import mimsoft.io.utils.TextModel

object PaymentTypeMapper {
    fun toDto(table: PaymentTypeTable?): PaymentTypeDto? {
        return if (table == null) null
        else PaymentTypeDto(
            id = table.id,
            name = table.name,
            icon = table.icon,
            title = TextModel(
                uz = table.titleUz,
                ru = table.titleRu,
                eng = table.titleEng
            )
        )
    }

    fun toTable(dto: PaymentTypeDto): PaymentTypeTable? {
        return if (dto == null) null
        else PaymentTypeTable(
            id = dto.id,
            name = dto.name,
            icon = dto.icon,
            titleUz = dto.title?.uz,
            titleRu = dto.title?.ru,
            titleEng = dto.title?.eng
        )
    }
}