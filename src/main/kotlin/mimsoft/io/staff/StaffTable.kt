package mimsoft.io.staff

import java.sql.Timestamp

const val STAFF_TABLE_NAME = "staff"
data class StaffTable(
    val id: Long? = null,
    val positionId: Long? = null,
    val phone: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: Timestamp? = null,
    val image: String? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null,
)