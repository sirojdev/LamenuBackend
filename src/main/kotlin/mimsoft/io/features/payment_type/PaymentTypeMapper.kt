package mimsoft.io.features.payment_type

import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.outcome_type.OutcomeTypeTable

object PaymentTypeMapper {
    fun toDto(table: PaymentTypeTable?): PaymentTypeDto? {
        return if (table == null) null
        else PaymentTypeDto(
            id = table.id,
            name = table.name,
            icon = table.icon
        )
    }

    fun toTable(dto: PaymentTypeDto): PaymentTypeTable? {
        return if (dto == null) null
        else PaymentTypeTable(
            id = dto.id,
            name = dto.name,
            icon = dto.icon
        )
    }
}