package mimsoft.io.services.sms

import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.utils.plugins.GSON

object SmsSenderService {
    private val smsGateway = SmsGatewayService
    suspend fun send(merchantId: Long?, phone: String, content: String?): String? {

        val merchant = smsGateway.get(merchantId)
        println("merchant-->${GSON.toJson(merchant)}")
        val provider = smsGateway.getProvider(merchant)
        println("\nprovider-->$provider")
        return provider?.send(phone = phone, content = content)
    }
}