package mimsoft.io.sms

data class SmsDto (
    val id: Long? = null,
    val clientId: Long? = null,
    val messageId: Long? = null,
    val time: String? = null,
    val status: Status? = null,
)

enum class Status {
    SENT, NOT_SENT
}