package mimsoft.io.features.sms_gateway
data class SmsGatewayDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val eskizEmail: String? = null,
  val eskizPassword: String? = null,
  val playMobileUsername: String? = null,
  val playMobilePassword: String? = null,
  val selected: String? = null
)

enum class SMSGatewaySelected {
  ESKIZ,
  PLAY_MOBILE
}
