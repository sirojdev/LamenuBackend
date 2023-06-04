package mimsoft.io.services.sms.providers.eskiz

import com.google.gson.annotations.SerializedName

data class MessageModel(
    val id: Long? = null,
    @SerializedName("mobile_phone")
    val phone: String? = null,
    val from: String? = null,
    val message: String? = null,
    val country: String? = "UZ",
)

data class EskizSendSmsResponse(
    val id: String? = null,
    val message: String? = null,
    val status: String? = null
)

data class EskizAuthBody(
    val email: String? = null,
    val password: String? = null,
)

data class EskizAccount(
    val email: String? = null,
    val password: String? = null
)

data class EskizAuthMessageData(
    val token: String? = null
)

data class EskizResponse(
    val message: String? = null,
    val data: EskizAuthMessageData? = null,
    val status: String? = null
)