package mimsoft.io.features.outcome

import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.staff.StaffDto

class OutcomeDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: String? = null,
    val staff: StaffDto? = null,
    val outcomeType: OutcomeTypeDto? = null
)