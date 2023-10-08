package mimsoft.io.features.outcome

import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.staff.StaffDto
import java.sql.Timestamp

const val OUTCOME_TABLE_NAME = "outcome"
data class OutcomeDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val amount: Double? = null,
    val name: String? = null,
    val staff: StaffDto? = null,
    val created: Timestamp? = null,
    val outcomeType: OutcomeTypeDto? = null
)