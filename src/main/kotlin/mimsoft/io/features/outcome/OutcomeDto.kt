package mimsoft.io.features.outcome

import java.sql.Timestamp
import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.staff.StaffDto

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
