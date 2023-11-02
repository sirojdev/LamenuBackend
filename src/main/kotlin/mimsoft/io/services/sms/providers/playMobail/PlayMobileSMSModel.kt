package mimsoft.io.services.sms.providers.playMobail

import com.google.gson.annotations.SerializedName

data class PlayMobileSMSModel(val messages: List<MessageModel>)

data class MessageModel(
  val recipient: String?,
  @SerializedName("message-id") val messageId: String?,
  val sms: SMSModel
)

data class SMSModel(val originator: String = "3700", val content: SMSContentModel?)

data class SMSContentModel(val text: String)
