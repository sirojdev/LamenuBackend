package mimsoft.io.services.sms

interface SmsProvider {
  suspend fun send(phone: String, content: String?): String?
}
