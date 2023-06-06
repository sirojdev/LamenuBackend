package mimsoft.io.features.outcome
import mimsoft.io.features.outcome_type.OutcomeTypeService
import mimsoft.io.features.staff.StaffService

object OutcomeMapper {
    suspend fun toOutcomeTable(outcomeDto: OutcomeDto?): OutcomeTable? {
        return if (outcomeDto == null) null
        else {
            val staff = StaffService.get(outcomeDto.staff?.phone)
            val outcomeType = outcomeDto.outcomeType?.id?.let { outcomeDto.merchantId?.let { it1 ->
                OutcomeTypeService.get(
                    it1, it)
            } }
            OutcomeTable(
                id = outcomeDto.id,
                merchantId = outcomeDto.merchantId,
                name = outcomeDto.name,
                staffId = staff?.id,
                outcomeTypeId = outcomeType?.id
            )
        }

    }

    suspend fun toOutcomeDto(outcomeTable: OutcomeTable?): OutcomeDto? {
        return if (outcomeTable == null) null
        else {
            val staffDto = StaffService.get(outcomeTable.staffId)
            val outcomeTypeDto = OutcomeTypeService.getById(outcomeTable.outcomeTypeId)
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