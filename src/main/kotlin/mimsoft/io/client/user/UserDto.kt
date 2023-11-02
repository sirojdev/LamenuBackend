package mimsoft.io.client.user

import java.sql.Timestamp
import mimsoft.io.features.badge.BadgeDto

data class UserDto(
  val id: Long? = null,
  val badge: BadgeDto? = null,
  val merchantId: Long? = null,
  val phone: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val image: String? = null,
  val birthDay: Timestamp? = null,
  val token: String? = null,
  val orderCount: Long? = null,
  val visitCount: Long? = null,
  val cashbackBalance: Double? = null
)
