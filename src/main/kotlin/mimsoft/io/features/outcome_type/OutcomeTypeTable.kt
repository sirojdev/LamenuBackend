package mimsoft.io.features.outcome_type

import java.sql.Timestamp

const val OUTCOME_TYPE_TABLE = "outcome_type"
data class OutcomeTypeTable(
    val id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val bgColor: String? = null,
    val textColor: String? = null,
    val merchantId: Long? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null
)
