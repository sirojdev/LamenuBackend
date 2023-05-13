package mimsoft.io.entities.client

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class UserDto(
    val id: Long? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val image: String? = null,
    val birthDay: String? = null
)
