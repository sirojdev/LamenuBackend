package mimsoft.io.courier.merchantChat

import kotlinx.serialization.Serializable
import java.sql.Timestamp

data class ChatMessageDto(
    val id: Long? = null,
    val message: String? = null,
    val from: Long? = null,
    val to: Long? = null,
    val type:MessageType?=null,
    val createdDate:Timestamp?=null
)