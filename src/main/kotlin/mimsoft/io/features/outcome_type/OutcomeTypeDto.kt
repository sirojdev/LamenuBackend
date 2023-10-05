package mimsoft.io.features.outcome_type

import mimsoft.io.utils.TextModel

data class OutcomeTypeDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val bgColor: String? = null,
    val textColor: String? = null
)