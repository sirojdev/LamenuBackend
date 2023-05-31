package mimsoft.io.entities.outcome

import mimsoft.io.entities.outcome_type.OutcomeTypeDto
import mimsoft.io.entities.staff.StaffDto

class OutcomeDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: String? = null,
    val staff: StaffDto? = null,
    val outcomeType: OutcomeTypeDto? = null
)