package mimsoft.io.features.notification

import java.sql.Timestamp
import mimsoft.io.client.user.UserDto
import mimsoft.io.utils.TextModel

data class NotificationDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val body: TextModel? = null,
  val title: TextModel? = null,
  val image: String? = null,
  val date: Timestamp? = null,
  val client: UserDto? = null,
  val isSendAndroid: Boolean? = null,
  val isSendIos: Boolean? = null,
  val isSendBot: Boolean? = null
)
