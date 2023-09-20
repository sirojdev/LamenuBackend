package mimsoft.io.integrate.onlinePbx

import com.google.gson.Gson
import mimsoft.io.utils.OkHttp

object OnPBXAuth {
    fun auth(): OnPBXAuthModel? {

        val request = OkHttp.request(
            mediaType = FORM_URLENCODED,
            body = "auth_key=$AUTH_KEY&new=true",
            url = AUTH_URL,
            method = POST
        )
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        val client = OkHttp.client(request)


        return Gson().fromJson(client, OnPBXAuthModel::class.java)
    }

    fun authMap(auth: OnPBXAuthModel? = null): Map<String?, String?> {
        return mapOf(auth?.data?.keyId to auth?.data?.key)
    }

}