package mimsoft.io.features.admin_sys

import java.sql.Timestamp

data class AdminSys(
    val phone: String? = null,
    val password: String? = null,
    val fullName: String? = null,
    val lastLogin: Timestamp? = null,
    val image: String? = null,
    val uuid: String? = null
)
