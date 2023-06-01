package mimsoft.io.entities.outcome
import java.sql.Timestamp

const val OUTCOME_TABLE_NAME = "outcome"
data class OutcomeTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: String? = null,
    val staffId: Long? = null,
    val outcomeTypeId: Long? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null
)
