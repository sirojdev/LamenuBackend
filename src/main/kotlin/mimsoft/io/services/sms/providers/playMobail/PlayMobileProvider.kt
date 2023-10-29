package mimsoft.io.services.sms.providers.playMobail

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import java.util.*
import mimsoft.io.services.sms.SmsProvider

class PlayMobileProvider(val merchantId: Long?, val username: String?, val password: String?) :
  SmsProvider {
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
    private const val sendSmsUrl = "https://send.smsxabar.uz/broker-api/send"
  }

  override suspend fun send(phone: String, content: String?): String {

    if (username == null || password == null) return "ERROR"

    val messageBody =
      PlayMobileSMSModel(
        messages =
          arrayListOf(
            MessageModel(
              recipient = phone.substringAfter("+"),
              messageId = UUID.randomUUID().toString(),
              sms = SMSModel(content = SMSContentModel(text = content ?: ""))
            )
          )
      )

    try {
      val response =
        client.post(sendSmsUrl) {
          contentType(ContentType.Application.Json)
          setBody(messageBody)
          basicAuth(username = username, password = password)
        }

      when (response.status) {
        HttpStatusCode.OK -> {
          return "DONE"
        }
        HttpStatusCode.Unauthorized -> {
          return "Unauthorized"
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return "ERROR"
  }
}
