package mimsoft.io.features.client_promo

import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto

data class ClientPromoDto(
  val id: Long? = null,
  val client: UserDto? = null,
  val promo: PromoDto? = null,
  val merchantId: Long? = null,
  val createdDate: Timestamp? = null,
  val endDate: Timestamp? = null
)
