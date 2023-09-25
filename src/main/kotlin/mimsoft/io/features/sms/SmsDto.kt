package mimsoft.io.features.sms

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.message.MessageDto

data class SmsDto (
    val id: Long? = null,
    val clientCount: Long? = null,
    val client: UserDto? = null,
    val message: MessageDto? = null,
    val time: String? = null,
    val status: Status? = null,
    val merchantId: Long? = null
)

enum class Status {
    SENT, NOT_SENT
}