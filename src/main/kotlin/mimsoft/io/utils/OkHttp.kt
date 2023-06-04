package mimsoft.io.utils

import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


object OkHttp {

    fun request(mediaType: String, body: Any, url: String, method: String): Request.Builder {


        return Request.Builder()
            .url(url.toHttpUrl().newBuilder().build())
            .method(
                method,
                Gson().toJson(body).toRequestBody(mediaType.toMediaTypeOrNull())
            )

    }

    fun client(request: Request): String? {
        val client = OkHttpClient().newCall(request).execute()
        val responseBody = client.body
        val body = responseBody?.string()
        client.close()
        return body
    }
}