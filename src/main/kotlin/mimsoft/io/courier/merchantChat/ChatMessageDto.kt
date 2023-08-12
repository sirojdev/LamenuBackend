package mimsoft.io.courier.merchantChat

import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class ChatMessageDto(
    val message: String? = null,
    val toId: Long?=null
)

data class ChatMessageSaveDto(
    val id: Long? = null,
    val message: String? = null,
    val fromId: Long? = null,
    val toId: Long? = null,
    val operatorId:Long?=null,
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