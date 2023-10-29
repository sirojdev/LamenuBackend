package mimsoft.io.services.sms.providers.eskiz

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.services.sms.SmsProvider

class EskizProvider(val merchantId: Long, val email: String?, val password: String?) : SmsProvider {

  val client =
    HttpClient(CIO) {
      install(ContentNegotiation) {
        gson {
          setPrettyPrinting()
          serializeNulls()
          setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
        }
      }
    }

  companion object {
    private const val authUrl = "https://notify.eskiz.uz/api/auth/login"
    private val tokensMap = mutableMapOf<Long, String?>()
    private const val sendSmsUrl = "https://notify.eskiz.uz/api/message/sms/send"
  }

  suspend fun auth(): String? {
    val response =
      client.post(authUrl) {
        contentType(ContentType.Application.Json)
        setBody(EskizAccount(password = password, email = email))
      }
    when (response.status) {
      HttpStatusCode.OK -> {
        val body = response.body<EskizResponse>()
        setToken(body.data?.token ?: "")
        return body.data?.token
      }
    }
    return "ERROR"
  }

  suspend fun update(): String? {
    val token = auth()
    tokensMap[merchantId] = token
    return token
  }

  private fun setToken(token: String) {
    tokensMap[merchantId] = token
  }

  private suspend fun getToken(): String? {
    return if (tokensMap.containsKey(merchantId)) {
      tokensMap[merchantId]
    } else {
      update()
    }
  }

  override suspend fun send(
    phone: String,
    content: String?,
  ): String {
    val message = MessageModel(phone = phone, message = content)
    try {
      val token = getToken() ?: return "ERROR"
      val response =
        client.post(sendSmsUrl) {
          contentType(ContentType.Application.Json)
          setBody(message)
          bearerAuth(token)
        }
      val body = response.body<EskizResponse>()

      when (body.status) {
        "token-invalid" -> {
          val token2 = auth() ?: return "ERROR"

          val response2 =
            client.post("https://notify.eskiz.uz/api/message/sms/send") {
              contentType(ContentType.Application.Json)
              setBody(message)
              bearerAuth(token2)
            }
          val body2 = response2.body<EskizResponse>()
          return when (body2.status) {
            "token-invalid" -> {
              "Unauthorized"
            }
            else -> "DONE"
          }
        }
        "else" -> {
          return "DONE"
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return "ERROR"
  }
}
