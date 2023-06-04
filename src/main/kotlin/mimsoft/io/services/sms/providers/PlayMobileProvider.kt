package mimsoft.io.services.sms.providers

import mimsoft.io.services.sms.SmsProvider

class PlayMobileProvider(val merchantId: Long?, val serviceId: String?, val key: String?) : SmsProvider {
    override suspend fun send(phone: String, content: String?): String {
        TODO("Not yet implemented")
    }

}