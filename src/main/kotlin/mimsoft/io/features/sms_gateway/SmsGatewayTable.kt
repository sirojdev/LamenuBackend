package mimsoft.io.features.sms_gateway

import java.sql.Timestamp
const val SMS_GATEWAY_TABLE = "sms_gateway"
data class SmsGatewayTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val eskizUsername: String? = null,
    val eskizPassword: String? = null,
    val playMobileUsername: String? = null,
    val playMobilePassword: String? = null,
    val selected: String? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null
)

