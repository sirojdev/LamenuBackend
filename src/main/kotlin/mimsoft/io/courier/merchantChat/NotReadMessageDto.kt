package mimsoft.io.courier.merchantChat

import java.sql.Timestamp

class NotReadMessageDto(
  val count: Long? = null,
  val from: Long? = null,
  val messages: List<UserMessage>? = null,
  val to: Long? = null,
)

class UserMessage(val content: String? = null, val time: Timestamp? = null)
