package mimsoft.io.features.message

import mimsoft.io.features.sms.SmsDto

data class MessageDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val content: String? = null,
    val time: String? = null,
    var smss: List<SmsDto?>? = null
)