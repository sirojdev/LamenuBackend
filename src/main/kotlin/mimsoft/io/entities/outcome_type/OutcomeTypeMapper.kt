package mimsoft.io.entities.outcome_type
object OutcomeTypeMapper {
    fun toOutcomeTypeTable(outcomeTypeDto: OutcomeTypeDto?): OutcomeTypeTable? {
        return if (outcomeTypeDto == null) null
        else OutcomeTypeTable(
            id = outcomeTypeDto.id,
            merchantId = outcomeTypeDto.merchantId,
            name = outcomeTypeDto.name
        )
    }

    fun toOutcomeTypeDto(outcomeTypeTable: OutcomeTypeTable?): OutcomeTypeDto? {
        return if (outcomeTypeTable == null) null
        else OutcomeTypeDto(
            id = outcomeTypeTable.id,
            merchantId = outcomeTypeTable.merchantId,
            name = outcomeTypeTable.name
        )
    }
}