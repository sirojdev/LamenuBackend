package mimsoft.io.entities.branch

import java.sql.Timestamp

const val BRANCH_TABLE_NAME = "branch"
data class BranchTable(
    var id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val address: String? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null,
)

