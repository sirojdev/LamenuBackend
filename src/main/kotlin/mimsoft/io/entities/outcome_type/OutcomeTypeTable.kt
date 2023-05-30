package mimsoft.io.entities.outcome_type

import java.sql.Timestamp

const val OUTCOME_TYPE_TABLE = "outcome_type"
data class OutcomeTypeTable(
    val id: Long? = null,
    val name: String? = null,
    val merchantId: Long? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null
)
