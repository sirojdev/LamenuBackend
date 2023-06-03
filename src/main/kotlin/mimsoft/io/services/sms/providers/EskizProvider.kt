package mimsoft.io.services.sms.providers

import mimsoft.io.services.sms.SmsProvider

class EskizProvider(serviceId: String?, key: String?) : SmsProvider{
    override suspend fun send(
        phone: String,
        content: String?,
    ) : String {
        println("EskizService.send")

        return "status"
    }

}