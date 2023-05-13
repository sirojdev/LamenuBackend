package mimsoft.io.entities.client

import java.sql.Timestamp

data class UserTable(
    val id: Long? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val image: String? = null,
    val birthDay: Timestamp? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
