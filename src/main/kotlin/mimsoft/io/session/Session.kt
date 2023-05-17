package mimsoft.io.session

import mimsoft.io.utils.Role
import java.sql.Timestamp

data class Session(
    val id: Long,
    val uuid: String,
    val deviceId: Long,
    val entityId: Long,
    val role: Role,
    val updated : Timestamp,
    val created: Timestamp,
    val isExpired: Boolean,
)
