package mimsoft.io.features.manager_sys

import java.sql.Timestamp

data class ManagerSysModel(
    val id: Long? = null,
    val phone: String? = null,
    val uuid: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val lastLogin: Timestamp? = null,
    val image: String? = null,
    val role: ManagerSysRole? = null,
    val created: Timestamp? = null
)