package mimsoft.io.courier.merchantChat

import java.sql.Timestamp
import kotlinx.serialization.Serializable
import mimsoft.io.features.staff.StaffDto

@Serializable
data class ChatMessageDto(
  val message: String? = null,
  val time: String? = null,
  val uuid: String? = null,
  val toId: Long? = null,
  val id: Long? = null
)

data class ChatMessageSaveDto(
  val id: Long? = null,
  val message: String? = null,
  val fromId: Long? = null,
  val toId: Long? = null,
  val operatorId: Long? = null,
  val operator: StaffDto? = null,
  val sender: Sender? = null,
  val time: Timestamp? = null
)

data class ChatMessageInfoDto(
  val lastMessage: String? = null,
  val count: Int? = null,
  val fromId: Long? = null,
  val toId: Long? = null,
  val sender: Sender? = null,
  val time: Timestamp? = null
)
