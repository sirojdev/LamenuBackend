package mimsoft.io.entities.sms_gateway

import java.sql.Timestamp
const val SMS_GATEWAY_TABLE = "sms_gateway"
data class SmsGatewayTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val eskizId: Long? = null,
    val eskizToken: String? = null,
    val playMobileServiceId: Long? = null,
    val playMobileKey: String? = null,
    val selected: String? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null
)