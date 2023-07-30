package mimsoft.io.client.user

import mimsoft.io.features.badge.BadgeDto
import java.sql.Timestamp


data class UserDto(
    val id: Long? = null,
    val badge: BadgeDto? = null,
    val merchantId: Long? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val image: String? = null,
    val birthDay: Timestamp? = null,
    val token : String? = null,
    val orderCount: Int? = null,
    val visitCount: Int? = null,
    val cashbackBalance: Double? = null
)
