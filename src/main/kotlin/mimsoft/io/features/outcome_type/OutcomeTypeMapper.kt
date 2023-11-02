package mimsoft.io.features.outcome_type

object OutcomeTypeMapper {
  fun toOutcomeTypeTable(outcomeTypeDto: OutcomeTypeDto?): OutcomeTypeTable? {
    return if (outcomeTypeDto == null) null
    else
      OutcomeTypeTable(
        id = outcomeTypeDto.id,
        merchantId = outcomeTypeDto.merchantId,
        nameUz = outcomeTypeDto.name?.uz,
        nameRu = outcomeTypeDto.name?.ru,
        nameEng = outcomeTypeDto.name?.eng,
        bgColor = outcomeTypeDto.bgColor,
        textColor = outcomeTypeDto.textColor
      )
  }
}
