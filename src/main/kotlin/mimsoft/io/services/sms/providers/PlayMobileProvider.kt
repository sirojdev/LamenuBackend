package mimsoft.io.services.sms.providers

import mimsoft.io.services.sms.SmsProvider

class PlayMobileProvider(serviceId: String?, key: String?) : SmsProvider {
    override suspend fun send(phone: String, content: String?): String {
        TODO("Not yet implemented")
    }

}