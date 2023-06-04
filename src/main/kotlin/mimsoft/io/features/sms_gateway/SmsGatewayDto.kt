package mimsoft.io.features.sms_gateway
data class SmsGatewayDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val eskizId: String? = null,
    val eskizToken: String? = null,
    val playMobileServiceId: String? = null,
    val playMobileKey: String? = null,
    val selected: String? = null
)

enum class SMSGatewaySelected {
    ESKIZ, PLAY_MOBILE
}