package mimsoft.io.features.message

import mimsoft.io.sms.SmsDto

data class MessageDto(
    val id: Long? = null,
    val content: String? = null,
    val time: String? = null,
    var smss: List<SmsDto?>? = null
)
