package mimsoft.io.services.eskiz

import com.google.gson.Gson
import mimsoft.io.onlinePbx.ACCEPT
import mimsoft.io.onlinePbx.CALLS_HISTORY_URL
import mimsoft.io.onlinePbx.POST
import mimsoft.io.utils.OkHttp
import mimsoft.io.utils.plugins.GSON

object Eskiz {

    data class EskizAuthBody(
        val email: String? = null,
        val password: String? = null,
    )

    data class EskizAuthMessageData(
        val token: String? = null
    )

    data class EskizAuthResponse(
        val message: String? = null,
        val data: EskizAuthMessageData? = null,
        val status : String? = null
    )

    private const val authUrl = "https://notify.eskiz.uz/api/auth/login"

    fun auth(email: String?, password: String?): String? {

        val authBody = EskizAuthBody(
            email = email,
            password = password
        )

        val request = OkHttp.request(
            mediaType = ACCEPT,
            body = authBody,
            url = authUrl,
            method = POST
        )
            .addHeader("Content-Type", ACCEPT)
            .build()

        val client = OkHttp.client(request)

        val data = Gson().fromJson(client, EskizAuthResponse::class.java)
        println("EskizService.auth: ${GSON.toJson(data)}")
        return data.data?.token
    }
}