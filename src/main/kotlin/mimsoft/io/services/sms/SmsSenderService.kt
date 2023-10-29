package mimsoft.io.services.sms

import mimsoft.io.features.sms_gateway.SmsGatewayService

object SmsSenderService {
  private val smsGateway = SmsGatewayService

  suspend fun send(merchantId: Long?, phone: String, content: String?): String? {

    val merchant = smsGateway.get(merchantId)
    val provider = smsGateway.getProvider(merchant)
    return provider?.send(phone = phone, content = content)
  }
}
