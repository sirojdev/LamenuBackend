package mimsoft.io.entities.sms_gateway
data class SmsGatewayDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val eskizId: Long? = null,
    val eskizToken: String? = null,
    val playMobileServiceId: Long? = null,
    val playMobileKey: String? = null,
    val selected: String? = null
)

enum class SMSGatewaySelected {
    ESKIZ, PLAY_MOBILE
}