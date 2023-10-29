package mimsoft.io.services.sms.providers.playMobail

import com.google.gson.annotations.SerializedName
import io.ktor.util.*
import java.util.*
import mimsoft.io.integrate.onlinePbx.ACCEPT
import mimsoft.io.integrate.onlinePbx.POST
import mimsoft.io.utils.OkHttp

object PlayMobileService {

  data class PlayMobileSMSModel(val messages: List<MessageModel>)

  data class MessageModel(
    val recipient: String?,
    @SerializedName("message-id") val messageId: String?,
    val sms: SMSModel
  )

  data class SMSModel(val originator: String = "3700", val content: SMSContentModel?)

  data class SMSContentModel(val text: String)

  private const val sendPlayMobileUrl = "https://send.smsxabar.uz/broker-api/send"

  suspend fun send(phone: String, content: String?, key: String?, serviceId: String?) {

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

    val request =
      OkHttp.request(mediaType = ACCEPT, body = messageBody, url = sendPlayMobileUrl, method = POST)
        .addHeader("Content-Type", ACCEPT)
        .addHeader("Authorization", "Basic ${"$serviceId:$key".encodeBase64()}")
        .build()

    val client = OkHttp.client(request)
    println("text sms: $client")

    //        val data = Gson().fromJson(client, PlayMobileSMSModel::class.java)

  }
}

suspend fun main() {
  PlayMobileService.send(
    phone = "998994277599",
    content = "Hello world",
    key = "cv23K6V5eP",
    serviceId = "itunity"
  )
}
