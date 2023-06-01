package mimsoft.io.entities.outcome
import mimsoft.io.entities.outcome_type.OutcomeTypeService
import mimsoft.io.entities.staff.StaffService2

object OutcomeMapper {
    suspend fun toOutcomeTable(outcomeDto: OutcomeDto?): OutcomeTable? {
        return if (outcomeDto == null) null
        else {
            val staff = StaffService2.get(outcomeDto.staff?.phone)
            val outcomeType = OutcomeTypeService.get(outcomeDto.outcomeType?.merchantId)
            OutcomeTable(
                id = outcomeDto.id,
                merchantId = outcomeDto.merchantId,
                name = outcomeDto.name,
                staffId = staff?.id,
                outcomeTypeId = outcomeType?.merchantId
            )
        }

    }

    suspend fun toOutcomeDto(outcomeTable: OutcomeTable?): OutcomeDto? {
        return if (outcomeTable == null) null
        else {
            val staffDto = StaffService2.get(outcomeTable.staffId)
            val outcomeTypeDto = OutcomeTypeService.get(outcomeTable.outcomeTypeId)
            OutcomeDto(
                id = outcomeTable.id,
                merchantId = outcomeTable.merchantId,
                name = outcomeTable.name,
                staff = staffDto,
                outcomeType = outcomeTypeDto
            )
        }
    }
}