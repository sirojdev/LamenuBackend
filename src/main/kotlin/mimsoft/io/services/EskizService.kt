package mimsoft.io.services

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import mimsoft.io.onlinePbx.*
import mimsoft.io.services.eskiz.Eskiz
import mimsoft.io.utils.OkHttp
import mimsoft.io.utils.plugins.GSON

object EskizService : SmsInterface {

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
        val status : String? = null
    )

    private const val sendSmsUrl = "https://notify.eskiz.uz/api/message/sms/send"
    override suspend fun send(
        phone: String,
        content: String?,
        key: String?,
        serviceId: String?
    ) {

        val message = MessageModel(
            phone = phone,
            message = content
        )

        val request = OkHttp.request(
            mediaType = ACCEPT,
            body = message,
            url = sendSmsUrl,
            method = POST
        )
            .addHeader("Content-Type", ACCEPT)
            .addHeader("Authorization", "Bearer ${Eskiz.auth(email = serviceId, password = key)}")
            .build()

        val client = OkHttp.client(request)

        val data = Gson().fromJson(client, EskizSendSmsResponse::class.java)
        println("EskizService.send: ${GSON.toJson(data)}")

    }

}

//suspend fun main() {
//    EskizService.send(
//        phone = "998994277599",
//        content = "Hello",
//        key = "UY3JYefSQ0EqxDBbBLLpJhTPhJrSeOdWYEBklJ7o",
//        serviceId = "sultonovakhror@gmail.com"
//    )
//}