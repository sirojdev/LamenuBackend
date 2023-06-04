package mimsoft.io.features.client

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mimsoft.io.features.badge.BadgeDto
import java.sql.Timestamp


data class UserDto(
    val id: Long? = null,
    val badge: BadgeDto? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val image: String? = null,
    val birthDay: String? = null
)
