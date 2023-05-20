package mimsoft.io.session

import mimsoft.io.utils.Role
import java.sql.Timestamp

const val SESSION_TABLE_NAME = "session"
data class SessionTable(
    val id: Long? = null,
    val phone: String? = null,
    val uuid: String? = null,
    val deviceId: Long? = null,
    val entityId: Long? = null,
    val role: String? = null,
    val updated : Timestamp? = null,
    val created: Timestamp? = null,
    val isExpired: Boolean? = null,
    val deleted: Boolean? = null
)