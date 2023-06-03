package mimsoft.io.services.sms

import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.sms_gateway.SMSGatewaySelected
import mimsoft.io.services.sms.providers.EskizProvider
import mimsoft.io.services.sms.providers.PlayMobileProvider

object SmsService {
    private val smsGateway = SmsGatewayService
    suspend fun send(merchantId: Long?, phone: String, content: String?) {

        val merchant = smsGateway.get(merchantId)
        val provider = smsGateway.getProvider(merchant)
        val status = provider?.send(phone = phone, content = content)


    }
}